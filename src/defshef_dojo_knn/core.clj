(ns defshef-dojo-knn.core
  (:require [clojure.string :as string]))

(defn load-dataset [url]
  (->>
   url
   slurp
   string/split-lines
   (map #(string/split % #"[,]"))
   (map (fn [row] {:features (map #(Double/parseDouble %) (butlast row)) :label (last row)}))))

(defn randomize [dataset]
  (shuffle dataset))

(defn dataset->folds [k dataset]
  (partition-all k dataset))

(defn train-test-seq
  ([folds]
   (train-test-seq (count folds) folds))
  ([remaining [holdout & training]]
   (when (< 0 remaining)
     (lazy-seq
      (cons {:holdout-set holdout :training-set (apply concat training)}
            (train-test-seq (dec remaining) (conj training holdout)))))))

(defn sqrt [x] (Math/sqrt x))

(defn pow [x y] (Math/pow x y))

(defn euclidian-distance-between [a b]
  (assert (= (count a) (count b)) "a and b must be the same size!")
  (->>
   (map (comp #(pow % 2) -) a b)
   (reduce + 0)
   sqrt))

(defn train-classifier
  "Train the classifier for k neighbours on the dataset. Produces a
  parametrized classifier function that takes a feature vector and
  returns a predicted label."
  [k training-dataset]
  (fn [datum]
    (->>
     training-dataset
     (map (fn [{:keys [features label]}]
            {:distance (euclidian-distance-between datum features)
             :label label}))
     (sort-by :distance)
     (take k)
     (group-by :label)
     (sort-by (fn [[_ neighbours-with-label]] (- (count neighbours-with-label))))
     first
     first)))

(defn test-classifier
  "Test a trained classifier against a labelled dataset. Produces a
  ratio of correct predictions vs. total predictions."
  [classify test-dataset]
  (let [correct-predictions (->>
                             test-dataset
                             (map (fn [test-datum]
                                    (= (:label test-datum) (classify (:features test-datum)))))
                             (filter true?)
                             count)]
    (/ correct-predictions (count test-dataset))))

(defn perform-step [k {:keys [holdout-set training-set] :as arg}]
  (let [trained-classifier (train-classifier k training-set)]
    (test-classifier trained-classifier holdout-set)))

(defn amean [coll]
  (/ (reduce + coll) (count coll)))

(defn evaluate-performance
  "Perform a leave-one-out validation for kNN algorithm with k neighbours"
  [k folds]
  (let [results (reduce
                 (fn [results step-dataset]
                   (if step-dataset
                     (conj results (perform-step k step-dataset))
                     results))
                 []
                 folds)]
    {:results results
     :mean (double (amean results))}))
