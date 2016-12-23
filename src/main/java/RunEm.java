import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.latlab.data.MixedDataSet;
import org.latlab.data.io.arff.ArffLoader;
import org.latlab.io.bif.BifParser;
import org.latlab.io.bif.BifWriter;
import org.latlab.learner.geast.Geast;
import org.latlab.learner.geast.IModelWithScore;
import org.latlab.learner.geast.Settings;
import org.latlab.model.Gltm;
import org.latlab.util.FileName;
import org.latlab.util.Variable;

public class RunEm {

	public static void main(String[] args) throws Exception {
		CommandLine line = null;
		Options options = getOptions();
		try {
			line = new GnuParser().parse(options, args);
		} catch (ParseException e) {
			System.out.println("Command line syntax error:" + e.getMessage());
			printUsage(options);
			return;
		}

		// if (args.length < 1) {
		// System.out.println("java GeastRunner data_file [initial_model]");
		// return;
		// }

		if (line.getArgs().length < 1) {
			printUsage(options);
			return;
		}

		String dataFileName = line.getArgs()[0];
		MixedDataSet data = ArffLoader.load(dataFileName);

		String dataName = FileName.getNameOfLastComponent(dataFileName);

		data.setClassVariable(getClassVariable(data, line));
		data.removeMissingInstances();

		StringBuilder originalLine = new StringBuilder("PltmEast");
		for (String arg : args) {
			originalLine.append(" " + arg);
		}

		// if (args.length > 1) {
		// BifParser parser = new BifParser(new FileInputStream(args[1]));
		// commandLine += " " + args[1];
		// initial = parser.parse(new Gltm());
		// data.synchronize(initial);
		// }

		Gltm initial = null;
		String initialModelName = line.getOptionValue('i', null);
		if (initialModelName != null) {
			BifParser parser =
					new BifParser(new FileInputStream(initialModelName));
			initial = parser.parse(new Gltm());
			data.synchronize(initial);
		} else {
			System.out.println("The input file must be specified by the -i option.");
			return;
		}

		String settingName = line.getOptionValue('s', "settings.xml");
		Settings settings = new Settings(settingName, data, dataName);
		Geast geast = settings.createGeast();
		geast.commandLine = originalLine.toString();

		String outputFileName = line.getOptionValue('o', "output.bif");

		try {
			IModelWithScore output = null;
			output = geast.context().estimationEm().estimate(initial);

			if (outputFileName != null && output != null) {
				new BifWriter(new FileOutputStream(outputFileName)).write(output);
			}
		} finally {
			geast.context().executor().shutdown();
		}
	}

	/**
	 * Returns the class variable given the command line options. May return
	 * {@code null} to indicate that no class variable is specified. Default to
	 * use the last variable in the data as class variable.
	 * 
	 * @param data
	 * @param line
	 * @return
	 */
	private static Variable getClassVariable(MixedDataSet data, CommandLine line) {

		// default is the last variable
		int classIndex = data.variables().size() - 1;

		if (!line.hasOption('c')) {
			return data.variables().get(classIndex);
		}

		String string = line.getOptionValue('c');
		if ("last".compareToIgnoreCase(string) == 0) {
			classIndex = data.variables().size() - 1;
		} else if ("first".compareToIgnoreCase(string) == 0) {
			classIndex = 0;
		} else if ("none".compareToIgnoreCase(string) == 0) {
			classIndex = -1;
		} else {
			try {
				classIndex = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Wrong number format for class variable index", e);
			}
		}

		if (classIndex < -1 || classIndex >= data.variables().size()) {
			throw new IllegalArgumentException(
					"Index of class variable is out of range");
		}

		return classIndex >= 0 ? data.variables().get(classIndex) : null;
	}

	@SuppressWarnings("static-access")
	private static Options getOptions() {
		Options options = new Options();

		options.addOption(OptionBuilder.hasArg().withArgName("model_file").withDescription(
				"start the search from an initial model").withLongOpt(
				"initial-model").create('i'));
		options.addOption(OptionBuilder.hasArg().withArgName("setting_file").withDescription(
				"use the specified settings file (default: settings.xml)").withLongOpt(
				"setting").create('s'));
		options.addOption(OptionBuilder.hasArg().withArgName("class_variable").withDescription(
				"specify the zero-based index of class variable, "
						+ "or none, first, last (default: last)").withLongOpt(
				"class").create('c'));
		options.addOption(OptionBuilder.hasArg().withArgName("output_file").withDescription(
				"specify the output BIF file (default: output.bif)").withLongOpt(
				"output-file").create('o'));

		return options;
	}

	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("RunEm [OPTION] data_file", options);
	}
}
