;; gorilla-repl.fileformat = 1

;; **
;;; # 5. UNCHUNKING
;; **

;; @@
(ns den-of-clojure.chunking)
;; @@

;; **
;;; It is possible to wrap a chunked seq in such a way to prevent it
;;; from chunking.
;;;
;;; *(Source, plus good discussion at
;;; http://stackoverflow.com/questions/3407876/how-do-i-avoid-clojures-chunking-behavior-for-lazy-seqs-that-i-want-to-short-ci)*
;;;
;;; Remember how basic lists (whether literal, or the result of cons
;;; calls) don't get chunked? This function uses that behavior:
;; **

;; @@
(defn unchunk [s]
  (when (seq s)
    (lazy-seq
     (cons (first s)
           (unchunk (next s))))))
;; @@

;; **
;;; Unchunking in action:
;; **

;; @@
(defn do-work [n]
  (println "Working on" n)
  (inc n))
;; @@

;; **
;;; Original:
;;;
;;; *Is Gorilla REPL showing all expected output here? Bug? Go to Emacs...*
;; **

;; @@
(take 1 (map do-work (range 100)))
;; @@

;; **
;;; Unchunked
;; **

;; @@
(take 1 (map do-work (unchunk (range 100))))
;; @@

;; @@
;; Be careful that you unchunk the right thing...
(take 1 (unchunk (map do-work (range 100))))
;; @@

;; **
;;; ---
;;; [prev](/worksheet.html?filename=src/den_of_clojure/sec4.clj)
;;; [top](/worksheet.html?filename=src/den_of_clojure/sec0.clj)
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec6.clj)
;; **
