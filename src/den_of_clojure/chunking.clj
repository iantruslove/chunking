(ns den-of-clojure.chunking)

;; 0. NOTES
;; - do this with Gorilla REPL?
;; - Move the ubersource plugin into this project and add notes to the README

;; 1. WHAT ARE SEQS?
;; What are seqs?
;; - implementations of clojure.lang.ISeq
;; - see ../../target/ubersource/clojure/1.6.0/clojure/lang/ISeq.java
;;   - first, next, cons

;; 2. SO WHAT ABOUT LAZY SEQS?
;; - Handy for dealing with unbounded input
;; lazy-seq generators (?)

;; 4. ...AND CHUNKED SEQS?
;; What's good about chunked seqs?
;; What's bad about chunked seqs?
;; - The (first (map print (range 100))) example

;; 5. WHAT TO NOT DO WITH CHUNKED SEQS

;; 6. UNCHUNKING
;; ...but be careful...

;; 7. BE EAGER, NOT LAZY (OR THE WAY OF THE REDUCE)
