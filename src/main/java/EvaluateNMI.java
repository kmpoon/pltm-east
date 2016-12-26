import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.latlab.data.MixedDataSet;
import org.latlab.data.io.arff.ArffLoader;
import org.latlab.io.bif.BifParser;
import org.latlab.learner.geast.DataPropagation;
import org.latlab.learner.geast.SharedTreePropagation;
import org.latlab.model.Gltm;
import org.latlab.reasoner.NaturalCliqueTreePropagation;
import org.latlab.util.DiscreteVariable;
import org.latlab.util.Function;
import org.latlab.util.Utils;

public class EvaluateNMI {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("EvaluateNMI [--allow-missing] data_file model_file [model_file...]");
			return;
		}
		
		int start = 0;
		boolean allowMissing = false;
		
		if (args[start].equals("--allow-missing")) {
			start++;
			allowMissing = true;
		}

		MixedDataSet data = ArffLoader.load(args[start]);
		start++;
		data.setClassVariableToLast();
		
		if (!allowMissing)
			data.removeMissingInstances();
		

		for (int i = start; i < args.length; i++) {
			Gltm model =
					new BifParser(new FileInputStream(args[i])).parse(new Gltm());
			data.synchronize(model);
			double nmi = evaluateNMI(data, model);
			System.out.println(args[i] + ": " + nmi);
		}
	}

	private static double evaluateNMI(MixedDataSet data, Gltm model) {
		DiscreteVariable classVariable = data.getClassVariable();

		List<DiscreteVariable> latents =
				new ArrayList<DiscreteVariable>(model.getInternalVars());

		List<Function> joints = new ArrayList<Function>(latents.size());
		for (DiscreteVariable latent : latents) {
			joints.add(Function.createFunction(Arrays.asList(classVariable,
					latent)));
		}

		DataPropagation propagations = new SharedTreePropagation(model, data);

		for (int i = 0; i < data.size(); i++) {
			int classLabel = (int) data.get(i).value(data.classIndex());
			Function classProbability =
					Function.createIndicatorFunction(classVariable, classLabel);

			NaturalCliqueTreePropagation propagation = propagations.compute(i);
			for (int l = 0; l < latents.size(); l++) {
				Function marginal = propagation.getMarginal(latents.get(l));
				Function joint = marginal.times(classProbability);
				joints.get(l).plus(joint);
			}
		}

		double max = -1;

		for (Function joint : joints) {
			joint.normalize();
			double nmi = Utils.computeNormalizedMutualInformation(joint);
			max = Math.max(max, nmi);
		}

		return max;
	}
}
