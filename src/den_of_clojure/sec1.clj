;; gorilla-repl.fileformat = 1

;; **
;;; # 1. WHAT ARE SEQS?
;;;
;;; * a basic abstraction of a list
;;;   * `first` / `rest` / `cons`
;;;   * `seq` function returns a seq on a collection
;; **

;; @@
(ns den-of-clojure.chunking)
;; @@

;; **
;;; Exercise the seq API:
;; **

;; @@
(def s (seq [1 2 3 4 5]))
(first s)
(rest s)
(cons 6 s)
;; @@

;; **
;;; You can get a seq from all kinds of collections (unordered
;;; collections will give an ordered seq).
;; **

;; @@
(first (seq '(1 2 3)))
(first (seq #{:c :B :1}))
(first (seq {:X 1 :Z 3 :p 5}))
(first (seq (.getBytes "foo")))
;; @@

;; **
;;; You can get a seq on Java collections
;; **

;; @@
(def string-bytes (.getBytes "foo"))
(type string-bytes)
;; ([B is an array of bytes)

(->> string-bytes
     seq                    ; get a seq on the byte array
     (cons 80)              ; cons a value of 80 onto the seq
     byte-array             ; create a clojure byte array from the seq
     String.)               ; construct a string
;; @@

;; **
;;; ## The details
;;; Seqs  are implementations of `clojure.lang.ISeq`
;;;
;;; See `../../target/ubersource/clojure/1.6.0/clojure/lang/ISeq.java`
;;; * `.first` / `.next` / `.cons`
;; **
