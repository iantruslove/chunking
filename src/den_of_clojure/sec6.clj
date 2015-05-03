;; gorilla-repl.fileformat = 1

;; **
;;; # 6. BE EAGER, NOT LAZY (OR THE WAY OF THE REDUCE)
;; **

;; @@
(ns den-of-clojure.chunking)
;; @@

;; @@
(defn do-work [n]
  (Thread/sleep 100)       ; some complex calculation
  (println "Worked on" n)) ; don't care about return val, only about side effects
;; @@

;; **
;;; Use seq fns (e.g. `doseq`, `doall`) to drive eager evaluation:
;; **

;; @@
(doall
 (map do-work (range 10)))
;; @@

;; **
;;; You can also use `reduce` to eagerly drive evaluation
;; **

;; @@
(reduce (fn [_ val] (do-work val))
        (range 10))
;; @@

;; **
;;; It's easy to clean up that previous reduce call with a macro:
;; **

;; @@
(defmacro reduce-over [f coll]
  `(reduce (fn [_# val#] (~f val#))
           ~coll))

(reduce-over do-work (range 10))
;; @@

;; **
;;; ---
;;; [prev](/worksheet.html?filename=src/den_of_clojure/sec5.clj)
;;; [top](/worksheet.html?filename=src/den_of_clojure/sec0.clj)
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec7.clj)
;; **
