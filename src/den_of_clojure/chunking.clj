(ns den-of-clojure.chunking
  (:require [clojure.java.io :as io]))

;; 0. NOTES
;; - ??? do this with Gorilla REPL?
;; - ??? Figure out some kind of eval-in-popup thinger?
;; - Save any live-coding parts out to chapter-by-chapter files, then
;;   M-x insert-file

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 1. WHAT ARE SEQS?
;; - a basic abstraction of a list
;;   - first rest cons
;; - seq function returns a seq on a collection

(def s (seq [1 2 3 4 5]))
(first s)
(rest s)
(cons 6 s)

(first (seq '(1 2 3)))
(first (seq #{:c :B :1}))
(first (seq {:X 1 :Z 3 :p 5}))
(first (seq (.getBytes "foo")))
(String. (byte-array (cons 80 (seq (.getBytes "foo")))))

;; The details - they are implementations of clojure.lang.ISeq
;; - see ../../target/ubersource/clojure/1.6.0/clojure/lang/ISeq.java
;;   - .first .next .cons

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 2. SO WHAT ABOUT LAZY SEQS?
;; To Infinity And Beyond!
;; - Handy for dealing with unbounded input
(def to-infinity (iterate inc 0))
(take 10 to-infinity)
(->> to-infinity
     (map #(* % %))
     (drop 100)
     (take 20))

;; - "unbounded" input might also come from a file
;;   - in this case, it's a 2MB file, but the approach would work (albeit slowly)
;;     for a file that's larger than memory.

;; http://www.esrl.noaa.gov/psd/boulder/data/boulderdaily.complete
;; has a record of daily weather measurements for >100 years. What's
;; the average temp for this day in the year?
(require '[clojure.java.io :as io]
         '[clojure.string :as string])

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

;; - Be wary when expecting side effects to happen
(def delayed-printing (map println (range 0 10)))

;; `doseq`, `doall` etc are the tools to use here

;; TODO: come up with a more realistic example - say open resource handles, or writing out files that don't get written until a lazy seq is realized



;; Generating lazy seqs
;; - A number of functions generate lazy seqs
;; - `lazy-seq` is the swiss army knife
;; - This will crop up again later on...
(defn lazy-fib [a b]
  (cons b
        (lazy-seq
         (lazy-fib b (+ a b)))))

(take 10 (lazy-fib 1 1))

;; Note the pattern of cons-ing things together, and using lazy-seq to
;; wrap the infinite recursion

;; Terminating sequences can be generated too
(defn countdown [n]
  (cons n
        (lazy-seq
         (if (pos? n)
           (countdown (dec n))
           nil))))

;; Note the pattern of cons-ing things together, and using lazy-seq to
;; wrap the recursive call

;; Could also do this in a `loop` form.



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 3. ...AND CHUNKED SEQS?

;; How many times will this print to the console?
(first (map print (range 100)))

;; It would be worse if, instead of just println, the function
;; actually did something with a more substantive side effect:
(defn do-work [n]
  (println "Retrieving item" n "from head of AMQP...")
  (Thread/sleep 100)
  (println "Returning processed result" n)
  {:contrived-example (* n n n)})

(defn write-results-to-db [coll]
  (println "Writing" coll))

(write-results-to-db
 (take-while #(< (:contrived-example %) 30)
             (map do-work (range 100))))

;; What's good about chunked seqs?
;; - optimization, to reduce the amount of machinery invoked to
;;  realize an item in a seq

;; What's bad about chunked seqs?
;; - See above - you can easily end up realizing more in the lazy seq
;;   than you realize. Need to be aware that this can happen,
;;   sometimes even if you're careful to avoid it.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 6. SPOTTING CHUNKING

;; Back to simple examples...
;; - collections
(chunked-seq? '(1 2))
(cons 0 (cons 1 nil))
(chunked-seq? (range 1000))
(chunked-seq? [1 2])

;; - seqs
(chunked-seq? (seq (range 1000)))
(chunked-seq? (seq [1 2]))

;; - seqs that don't get chunked
(chunked-seq? (seq '(0 1)))
(chunked-seq? (seq (cons 0 (cons 1 nil))))

;; Er???
(chunked-seq? (map println (range 100)))

;; Remember the example that plainly exhibits the chunking:
(first (map println (range 100)))

;; Why not?! Look at the implementation of map

;; So you can't inspect a return value collection to determine whether
;; chunks have been evaluated in order to calculate that value


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 5. UNCHUNKING
;; - Good discussion at http://stackoverflow.com/questions/3407876/how-do-i-avoid-clojures-chunking-behavior-for-lazy-seqs-that-i-want-to-short-ci

;; Remember how basic lists (whether literal, or the result of cons
;; calls) don't get chunked?
(defn unchunk [s]
  (when (seq s)
    (lazy-seq
     (cons (first s)
           (unchunk (next s))))))

;; Original
(take 1 (map println (range 100)))

;; Unchunked
(take 1 (map println (unchunk (range 100))))

;; Be careful...
(take 1 (unchunk (map println (range 100))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 6. BE EAGER, NOT LAZY (OR THE WAY OF THE REDUCE)

(defn do-work [n]
  (java.lang.Thread/sleep 100)          ; some complex calculation
  (println "Worked on" n)) ; don't care about return val, only about side effects

;; with seq fns
(doall
 (map do-work (range 10)))

;; using reduce to eagerly drive evaluation
(reduce (fn [_ val] (do-work val))
        (range 10))

;; clean up the reduce call with a macro
(defmacro reduce-over [f coll]
  `(reduce (fn [_# val#] (~f val#))
           ~coll))

(reduce-over do-work (range 10))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 7. REDUCERS

;; What are reducers?
;; - clojure.reducers namespace

;; - some really interesting resources:
;;   - Rich Hickey's two blog posts
;;     - http://clojure.com/blog/2012/05/08/reducers-a-library-and-model-for-collection-processing.html
;;     - http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html
;;   - Rich's EuroClojure talk
;;     - https://vimeo.com/45561411
;;   - Renzo Borgatti's StrangeLoop talk
;;     - http://www.infoq.com/presentations/clojure-reducers
;;   - Aphyr's Tesser library has some really interesting ideas
;;     - https://github.com/aphyr/tesser




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 8. TRANSDUCERS
;; - Understand Reducers first
;; - 
