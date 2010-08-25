package net.sourceforge.barcodetickets.api;

// this project uses the Apache Commons CLI library
import org.apache.commons.cli.*;

import java.util.TreeMap;
import java.net.URLEncoder;

/**
 * Generates a BTS API request signature from the parameters provided upon
 * launch of this program on the command line, as a debugging tool for
 * developers producing API clients.
 * 
 * @license Apache License 2.0
 * @author Frederick Ding
 * @version $Id$
 */
public class SignatureCmdLine {

	static Options cliOptions = new Options();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// set up how to deal with CLI args
		cliOptions.addOption("verb", true, "HTTP verb (GET, POST, PUT, HEAD)");
		cliOptions.addOption("hostname", true, "hostname");
		cliOptions.addOption("uri", true, "requested URI including starting /");
		cliOptions.addOption("param", true, "a parameter");
		cliOptions.addOption("key", true, "the API key for the HMAC hash");
		cliOptions.addOption("urlencode", false,
				"use URL encode on the signature?");
		cliOptions.addOption("help", false, "show help");

		// parse the command line arguments
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(cliOptions, args);
		} catch (ParseException e1) {
			System.out.println("Oops, we ran into a problem: "
					+ e1.getMessage());
			help();
			return;
		}

		// show help
		if (cmd.hasOption("help")) {
			help();
			return;
		}

		// required parameters
		if (!cmd.hasOption("hostname") || !cmd.hasOption("uri")
				|| !cmd.hasOption("key")) {
			System.out
					.println("The hostname, uri and key parameters are required.\n");
			help();
			return;
		}

		// add the parsed params to our map
		TreeMap<String, String> params = new TreeMap<String, String>();
		for (String oneParam : cmd.getOptionValues("param")) {
			String[] parts = oneParam.split("=");
			params.put(parts[0], parts[1]);
		}

		// initialize the Signature class
		Signature sigGen = new Signature(cmd.getOptionValue("key"));

		try {
			String signature = sigGen.generate(
					cmd.getOptionValue("verb", "GET"),
					cmd.getOptionValue("hostname"), cmd.getOptionValue("uri"),
					params);
			if (cmd.hasOption("urlencode")) {
				System.out.println(URLEncoder.encode(signature, "UTF-8"));
			} else
				System.out.println(signature);
		} catch (Exception e) {
			System.out
					.println("An error occurred in the signature generation class: "
							+ e.getMessage());
		}

	}

	public static void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptPrefix("--");
		formatter.printHelp("java -jar SignatureCmdLine.jar [options]",
				cliOptions);
		return;
	}

}
