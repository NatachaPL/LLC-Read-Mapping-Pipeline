# LLC-Read-Mapping-Pipeline
Implementation code for my MSc in Bioinformatics thesis project.

The goal was to develop a mapping tool with high coverage and precision by adopting hash-based approaches to increase the search space over the reference genome with multiple keys for each read, taking into account quality information and biological constraints to generate them. The mapping will be definitive after a positive alignment of the read with the genome, using algorithms based in the Needleman-Wunsch method.

Some of the algorithms implemented were based on the GNUMAP (http://seqanswers.com/wiki/Gnumap).

The config.properties file contains the parameters required by the classes.
