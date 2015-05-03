;; gorilla-repl.fileformat = 1

;; **
;;; # 2. SO WHAT ABOUT LAZY SEQS?
;; **

;; @@
(ns den-of-clojure.chunking
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import (java.util UUID)))
;; @@

;; **
;;; To Infinity And Beyond!
;;;
;;; Lazy seqs are really handy for dealing with unbounded input:
;; **

;; @@
(def to-infinity (iterate inc 0))

(take 10 to-infinity)

;; Run some calculations on the seq:
(->> to-infinity
     (map #(* % %))
     (drop 100)
     (take 20))
;; @@

;; **
;;; ## Example: What's today's average temperature?
;;;
;;; Realistically "unbounded" input might also come from a file. Data
;;; read from a stream can easily overwhelm memory resources.
;;;
;;; http://www.esrl.noaa.gov/psd/boulder/data/boulderdaily.complete
;;; contains a record of daily weather measurements for >100 years.
;;; It's a 2MB file, but this approach would work (albeit slowly) for a
;;; file that's larger than memory.  What's average temp for this day
;;; in the year?
;;;
;;; Here's an excerpt of the file's contents:
;;; ```
;;; 1898  4 10     63     38    0.00  -998.0   -998
;;; 1898  4 11     57     42    0.00  -998.0   -998
;;; 1898  4 12     47     25    0.37  -998.0   -998
;;; 1898  4 13     68     35    0.00  -998.0   -998
;;; 1898  4 14     74     49    0.00  -998.0   -998
;;; 1898  4 15     76     52    0.00  -998.0   -998
;;; ```
;; **

;; @@
(let [url "http://www.esrl.noaa.gov/psd/boulder/data/boulderdaily.complete"]
  (with-open [rdr (io/reader url)]
    (->> (line-seq rdr)
         ;; Only work on lines for today's date (5/7)
         (filter (partial re-find #"^ ....  5  7"))
         ;; Grab the day's max temp field
         (map #(-> %
                   (string/split #"[ ]+")
                   (nth 4)))
         ;; parse int
         (map #(Integer/parseInt %))
         ;; sum and count
         (reduce (fn [[total-sum total-count] this-temp]
                   [(+ total-sum this-temp) (inc total-count)])
                 [0 0])
         ;; average
         (apply /)
         float)))
;; @@

;; **
;;; Be wary if you're unintentionally expecting side effects to happen eagerly...
;; **

;; @@
(def input-data (range 0 10))

(defn insert-item
  "Returns the DB ID of the inserted item"
  [data]
  (println "inserting" data)
  (Thread/sleep 100)
  (UUID/randomUUID))       ; Just imagine that this is the ID...

(def db-ids (map insert-item input-data))
;; @@

;; @@
(println db-ids)
;; @@

;; **
;;; Perhaps that matters, perhaps it does not.
;;;
;;; `doseq`, `doall` etc are the tools to use in the case that you do want to force side effects to occur eagerly.
;; **

;; @@
(def db-ids-2 (doall (map insert-item input-data)))
;; @@

;; **
;;; **TODO:** come up with a more realistic example - say open resource handles, or writing out files that don't get written until a lazy seq is realized
;; **

;; **
;;; ## Generating lazy seqs
;;; A number of functions generate lazy seqs.
;;; `lazy-seq` is the swiss army knife.
;;;
;;; *Take note, this will crop up again later on...*
;; **

;; @@
(defn lazy-fib [a b]
  (cons b
        (lazy-seq
         (lazy-fib b (+ a b)))))
;; @@

;; @@
(take 10 (lazy-fib 1 1))
;; @@

;; **
;;; Note the pattern of cons-ing things together, and using lazy-seq to
;;; wrap the infinite recursion
;; **

;; **
;;; Terminating sequences can be generated too:
;; **

;; @@
(defn countdown [n]
  (cons n
        (lazy-seq
         (if (pos? n)
           (countdown (dec n))
           nil))))

(countdown 10)
;; @@

;; **
;;; Note the pattern of cons-ing things together, and using lazy-seq to
;;; wrap the recursive call.
;;; You could also do this in a `loop` form.
;; **

;; **
;;; ---
;;; [prev](/worksheet.html?filename=src/den_of_clojure/sec1.clj)
;;; [top](/worksheet.html?filename=src/den_of_clojure/sec0.clj)
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec3.clj)
;; **
