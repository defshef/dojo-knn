# defshef-dojo-knn

Instructions and tips for (def shef 33) kNN classifier dojo

# kNN Algorithm

Google "kNN" or "k nearest neighbour" for explanations of the algorithm. [Wikipedia](https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm) has detailed information, other sources like [Machine Learning Mastery](http://machinelearningmastery.com/k-nearest-neighbors-for-machine-learning/) may be easier for a crash course.

Essentially, the naive kNN classifier considers data as points in a space - each point has co-ordinates and a label. The co-ordinates are known as "features". This approach means you can measure the distance between any two points in that space, and so you can find the "nearest neighbours" of any point. The algorithm performs this calculation for an unlabelled datum and classifies the unlabelled point the same as the majority of its "k" nearest neighbours. Simple!

We'll use the classic "Iris" data set to build and test our algorithms. It's provided by the [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets/Iris) and it's a comma-separated table with rows like this:

`5.1,3.5,1.4,0.2,Iris-setosa`

The four numbers are features of the flower, things like petal width and stamen length. The last string is a label - this row is measurements of [Iris Setosa](https://en.wikipedia.org/wiki/Iris_setosa).

I suggest we use this dataset because you don't need to know what the data means to get the algorithm working, we know we should be able to get good predictive power from it, and it's small - 150 rows - so we can start with writing a completely naive classifier and focus on the machine learning and functional programming aspects, without worrying about performance.

Your task, should you choose to accept it, is:
* to write a kNN classifier and the necessary scaffolding to run it against the Iris data set, using [leave-one-out cross-validation](https://en.wikipedia.org/wiki/Cross-validation_(statistics)#Leave-one-out_cross-validation) to produce a mean correct predictions for a given value of k. (Leave-one-out cross-validation means split the randomised dataset up into k "folds", then for each fold f, train on the rest of the dataset and use f to test the classifier)
* bonus points - write a test that runs your classifier with a known "k" value and a known data set to produce a known result (harder than you might think!)

The following is a suggested breakdown - feel free to completely ignore it!

* Parse the data set into numeric features and labels
* Write an algorithm to "train" your classifier that takes a value for k and a training dataset
* Write an algorithm to "test" your classifier that takes the output of your "train" algorithm and a test dataset, predicts the labels of the test dataset, and returns a % correct predictions
* Write an algorithm to run your train/test cycle until all the "folds" have been used as a test dataset, and average the % correct predictions

There's a quick implementation I did in Clojure included in this project. I provide it here in case you get completely stuck on something!

Once you have a working classifier, there's a whole bunch of things you can play with, for example:
* optimising for a larger dataset (I suggest the [Phishing Websites dataset](https://archive.ics.uci.edu/ml/datasets/Phishing+Websites) with 11 features and several thousand rows, as it has numeric attributes so will work if you assumed numeric attributes...)
* find ways to take advantage of the "embarrassingly parallel" nature of the problem
* check out different distance functions
* check out the impact of ignoring subsets of features
* see how your classifier does with different data sets
* supporting categorical data to try out data sets like the Titanic Survivors data set
* evaluating performance with just the % correct predictions is pretty basic, try better performance measures like AUC
* try more validation approaches instead of just leave-one-out cross-validation
* anything you want to play with!

Copyright © 2017 Paul Brabban

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
