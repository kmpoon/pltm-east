import org.latlab.analysis.miCurve.ClassificationComputer;
import org.latlab.util.FileName;

public class Classify {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Classify [--allow-missing] data_file model_file");
			return;
		}

		int start = 0;
		boolean allowMissing = false;

		if (args[start].equals("--allow-missing")) {
			start++;
			allowMissing = true;
		}

		String dataFile = args[start];
		String modelFile = args[start + 1];
		String name = FileName.getName(modelFile);

		ClassificationComputer.run(modelFile, dataFile, name, allowMissing);
	}
}
