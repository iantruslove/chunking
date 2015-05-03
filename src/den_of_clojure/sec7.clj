;; gorilla-repl.fileformat = 1

;; **
;;; # 7. REDUCERS
;;;
;;; ## What are reducers?
;;; An alternative way to manipulate collections.
;;;
;;; From the docs:
;;; > *Sequence functions are typically applied lazily, in order, create
;;; > intermediate results, and in a single thread.*
;;;
;;; and
;;;
;;; > *A reducer is the combination of a reducible collection  with a
;;; > reducing function.*
;;;
;;; Execution is deferred until the final reduction - for which you
;;; can use `reduce`, `into`, or `r/foldcat`.
;;;
;;; Implemented as a library in the `clojure.reducers` namespace.
;; **

;; @@
(ns den-of-clojure.chunking
  (:require [clojure.core.reducers :as r]))
;; @@

;; @@
(def rr (r/map inc (range 20)))

rr
;; @@

;; @@
(reduce + rr)

(into #{} rr)

(r/foldcat rr)
;; @@

;; @@
(r/foldcat (r/map (partial * 2) rr))
;; @@

;; @@
(def fc (r/foldcat (r/map (partial * 2) rr)))

(reduce + fc)

(seq fc)
;; @@

;; **
;;; ## Quick reducers example
;;;
;;; Back in section 3, we had this example:
;; **

;; @@
(def starting-values (vec (range 0 32)))

(defn lookup-item [n]
  (let [query-cost (* n n)]
    (println "Looking up" n)
    (Thread/sleep query-cost)
    {:query-cost query-cost}))

(defn valid-result? [result]
  (< (:query-cost result) 70))

(defn use-valid-results [results]
  (println "Results:" results))

(use-valid-results
 (take-while valid-result?
             (map lookup-item starting-values)))
;; @@

;; **
;;; Rewriting that using reducers looks very similar (I added a `time`
;;; call and used `->>` to make it a little more readable; it's
;;; identical otherwise):
;; **

;; @@
(->> starting-values
     (r/map lookup-item)
     (r/take-while valid-result?)
     (into [])
     use-valid-results
     time)
;; @@

;; **
;;; Reducers are composable (but note that the order of operation is
;;; the same as `->`):
;; **

;; @@
(def all-valid-results
  (comp (r/take-while valid-result?)
        (r/map lookup-item)))

(use-valid-results
 (into [] (all-valid-results starting-values)))
;; @@

;; **
;;; Reducers are also trivially parallelizable.
;;;
;;; This variant evaluates all starting values and **filters** those
;;; that are valid. Since the worst case sleep is 961msecs,
;;; partitioning and parallelizing the computation yields really good
;;; results.
;; **

;; @@
(def combinef
  (r/monoid               ; a helper for generate combining fns
   into                   ; the combine operation
   (constantly [])))      ; the identity constructor

(def num-per-partition 10)

(time
 (->> (shuffle starting-values)
      (r/map lookup-item)
      (r/filter valid-result?)
      (r/fold num-per-partition
              combinef               ; partition combining fn
              conj)))                ; reducing fn
;; @@

;; **
;;; ## Resources:
;;; - Rich Hickey's two blog posts
;;;   - http://clojure.com/blog/2012/05/08/reducers-a-library-and-model-for-collection-processing.html
;;;   - http://clojure.com/blog/2012/05/15/anatomy-of-reducer.html
;;; - Rich's EuroClojure talk
;;;   - https://vimeo.com/45561411
;;; - Renzo Borgatti's StrangeLoop talk
;;;   - http://www.infoq.com/presentations/clojure-reducers
;;; - Aphyr's Tesser library has some really interesting ideas
;;;   - https://github.com/aphyr/tesser
;; **

;; **
;;; ---
;;; [prev](/worksheet.html?filename=src/den_of_clojure/sec6.clj)
;;; [top](/worksheet.html?filename=src/den_of_clojure/sec0.clj)
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec8.clj)
;; **
