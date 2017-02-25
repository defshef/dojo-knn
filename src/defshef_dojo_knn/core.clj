(ns defshef-dojo-knn.core
  (:require [clojure.string :as string]))

(defn load-dataset [url]
  (->>
   url
   slurp
   string/split-lines
   (map #(string/split % #","))))

(defn split-randomized-dataset [k dataset]
  (partition-all k (shuffle dataset)))

(defn train-test-seq
  ([folds]
   (train-test-seq (count folds) folds))
  ([remaining [holdout & training]]
   (when (< 0 remaining)
     (lazy-seq
      (cons {:holdout holdout :training training} (train-test-seq (dec remaining) (conj training holdout)))))))

(defn train-classifier
  "Train the classifier on the dataset. Produces a parametrized classifier."
  [training-dataset])

(defn test-classifier
  "Test a trained classifier against a labelled dataset. Produces a percentage correct predictions."
  [model test-dataset]
  0)

(defn perform-step [{:keys [holdout training]}]
  (->>
   training
   train-classifier
   (test-classifier holdout)))

(defn amean [coll]
  (/ (reduce + coll) (count coll)))

(defn run-experiment [folds]
  (let [results (reduce
                 (fn [results step-dataset]
                   (if step-dataset
                     (conj results (perform-step step-dataset))
                     results))
                 []
                 (train-test-seq folds))]
    {:results results
     :mean (amean results)}))
