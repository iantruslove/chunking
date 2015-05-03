;; gorilla-repl.fileformat = 1

;; **
;;; # 7. REDUCERS
;;;
;;; What are reducers?
;;; - clojure.reducers namespace
;; **

;; @@
(ns den-of-clojure.chunking
  (:require [clojure.core.reducers :as r]))
;; @@

;; **
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
;;; Rewriting that using reducers looks very similar:
;; **

;; @@
(time
 (use-valid-results
  (into []
        (r/take-while valid-result?
                      (r/map lookup-item starting-values)))))
;; @@

;; **
;;; Reducers are composable:
;; **

;; @@
(time
 (use-valid-results
  (into [] ((comp (r/take-while valid-result?)
                  (r/map lookup-item))
            starting-values))))
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
(defn combinef
  ([]
   [])                                  ; return identity
  ([acc val]
   (into acc val)))                     ; combine

(def num-per-partition 10)

(time
 (r/fold num-per-partition
         combinef                       ; partition combining fn
         conj                           ; reducing fn
         (r/filter valid-result?
                   (r/map lookup-item (shuffle starting-values)))))
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
