# pltm-east

This directory contains the Java implementation of the PLTM algorithm described
in the paper "Variable selection in model-based clustering: To do or to 
facilitate" (ICML 2010).  It also contains the sub-directory data where
data sets in ARFF format can be found.

The directory includes shell scripts for running on Unix machines.  

To learn PLTM (named `output.bif`) on a data set (e.g. `data/iris.arff`):

```bash
$ ./run.sh data/iris.arff
```

To show the options of PLTM-EAST, type `run.sh` without any parameters:

```bash
$ ./run.sh 
usage: PltmEast [OPTION] data_file
 -c,--class <class_variable>       specify the zero-based index of class
                                   variable, or none, first, last
                                   (default: last)
 -i,--initial-model <model_file>   start the search from an initial model
 -m,--allow-missing                allow missing data
 -o,--output-file <output_file>    specify the output BIF file (default:
                                   output.bif)
 -s,--setting <setting_file>       use the specified settings file
                                   (default: settings.xml)
```                                   

To specify the output file name:

```bash
$ ./run.sh -o iris.bif data/iris.arff
```

To evaluate a model using NMI:

```bash
$ ./evaluateNMI.sh data/iris.arff iris.bif
```

This script also accepts multiple model files, such as:

```bash
$ ./evaluateNMI.sh data/iris.arff iris*.bif
```

In case other measures are used for evaluating the clusterings, the marginal 
probabilities of the latent variables for every data cases can be computed by:

```bash
$ ./classify.sh data/iris.arff iris.bif
```

It will generate several CSV files, each of which contains the marginal 
probabilities of a latent variable for every data case.  It will also
generate a CSV file for the class variable.

## Compile

To compile the source code and create the distribution directory, type the 
following command in the project directory:

```bash
$ ant dist
```

The distribution directory can be found in the `dist` directory.

To compile it using Eclipse, you will need to install the [JavaCC Eclipse Plug-in](http://eclipse-javacc.sourceforge.net).

## Enquiry

For any inquiries, please email kmpoon@eduhk.hk.

## References

- Leonard K. M. Poon, Nevin L. Zhang, Tao Chen, and Yi Wang (2010). [Variable Selection in Model-Based Clustering: To Do or To Facilitate](http://www.cse.ust.hk/~lkmpoon/papers/pltm-icml10.pdf). ICML-2010.
- Leonard K. M. Poon, Nevin L. Zhang, Tengfei Liu, and April H. Liu (2012). [Model-Based Clustering of High-Dimensional Data: Variable Selection versus Facet Determination](http://www.cse.ust.hk/~lkmpoon/papers/ijar2012.pdf). IJAR.
