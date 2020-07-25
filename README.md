# HA-MLCS
## Introduction
We propose a novel *MLCS* (<u>**M**</u>ultiple <u>**L**</u>ongest <u>**C**</u>ommon <u>**S**</u>ubsequence) algorithm **HA-MLCS** for COVID-19 big sequence data similarity analysis.

## Install & Run

 1. Install JDK-1.8;
 2. Put the data files to be processed into the 'file' folder of this project;
 3. Run the following command in a shell command window.
```
java -jar -Dmlcs.max-thread=2 -Djava.util.Arrays.useLegacyMergeSort=true -Dmlcs.rt=1 HA-MLCS.jar [fileName]
```
 - [fileName]: program parameter, files should be put in 'file' Folder to be read;
 - [-Dmlcs.max-thread]: VM parameter, the number of threads you need to start;
 - [-Djava.util.Arrays.useLegacyMergeSort]: VM parameter, should be set true;
 - [-Dmlcs.rt]: VM parameter, is a user-customized parameter ($1 \leqslant rt \in \mathbb{Z}$), which represents how many number of key points to be retained in each layer when constructing *MLCS-ODAG*.
