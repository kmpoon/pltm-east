import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.latlab.analysis.miCurve.ClassificationComputer;
import org.latlab.util.FileName;

public class Classify {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Classify [--allow-missing] [--no-class] data_file model_file");
			return;
		}

		int start = 0;
		boolean allowMissing = false;
		boolean noClass = false;

		for (start = 0; start < args.length; start++) {
			if (args[start].equals("--allow-missing")) {
				allowMissing = true;
			} else if (args[start].equals("--no-class")) {
				noClass = true;
			} else {
				break;
			}
		}

		String dataFile = args[start];
		String modelFile = args[start + 1];
		String name = FileName.getName(modelFile);

		ClassificationComputer.run(modelFile, dataFile, name, allowMissing, noClass);
	}

}
