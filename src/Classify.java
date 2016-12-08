import org.latlab.analysis.miCurve.ClassificationComputer;
import org.latlab.util.FileName;

public class Classify {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("Classify data_file model_file");
			return;
		}

		String name = FileName.getName(args[1]);

		ClassificationComputer.main(new String[] { args[1], args[0], name });
	}
}
