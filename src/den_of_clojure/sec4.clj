;; gorilla-repl.fileformat = 1

;; **
;;; # 4. IDENTIFYING CHUNKED SEQS
;; **

;; @@
(ns den-of-clojure.chunking)
;; @@

;; **
;;; Back to simple examples here...
;;;
;;; Are collections chunked?
;; **

;; @@
(chunked-seq? '(1 2))
;; @@

;; @@
(chunked-seq? (cons 3 (cons 2 (cons 1 nil))))
;; @@

;; @@
(chunked-seq? [1 2])
;; @@

;; @@
(chunked-seq? #{1 2 3})
;; @@

;; **
;;; Are seqs chunked?
;; **

;; @@
(chunked-seq? (range 1000))
;; @@

;; @@
(chunked-seq? (seq (range 1000)))
;; @@

;; @@
(chunked-seq? (seq [1 2]))
;; @@

;; @@
(chunked-seq? (seq (vec (range 100))))
;; @@

;; @@
(chunked-seq? (seq #{1 2}))
;; @@

;; @@
(chunked-seq? (seq '(0 1 2)))
;; @@

;; @@
(chunked-seq? (seq (apply list (range 100))))
;; @@

;; @@
(chunked-seq? (seq (cons 3 (cons 2 (cons 1 nil)))))
;; @@

;; **
;;; Remember the example that plainly exhibits the chunking:
;; **

;; @@
(chunked-seq? (vec (range 100)))

(first (map println (vec (range 100))))

(chunked-seq? (map println (vec (range 100))))
;; @@

;; **
;;; Why are neither the input nor the output chunked, but it behaves chunked?
;;;
;;; Look at the implementation of map
;; **

;; @@
(defn map
  ([f coll]
   (lazy-seq
    (when-let [s (seq coll)]
      (if (chunked-seq? s)
        (let [c (chunk-first s)
              size (int (count c))
              b (chunk-buffer size)]
          (dotimes [i size]
              (chunk-append b (f (.nth c i))))
          (chunk-cons (chunk b) (map f (chunk-rest s))))
        (cons (f (first s)) (map f (rest s)))))))
  ;; other arities elided
  )
;; @@

;; **
;;; So you can't inspect a return value collection to determine whether
;;; chunks have been evaluated in order to calculate that value.
;;;
;;; You need to understand which types of collections, when a seq is
;;; created on them, will return a chunked seq!
;; **

;; **
;;; ---
;;; [prev](/worksheet.html?filename=src/den_of_clojure/sec3.clj)
;;; [top](/worksheet.html?filename=src/den_of_clojure/sec0.clj)
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec5.clj)
;; **
