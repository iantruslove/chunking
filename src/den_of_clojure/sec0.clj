;; gorilla-repl.fileformat = 1

;; **
;;; # LAZY SEQS CAN BITE!
;;;
;;; Have you ever used an impure function in one of Clojure’s common seq operations (like map)?
;;;
;;; Have you ever created a seq from a source that depends on system resources (like network, or disk)?
;;;
;;; Probably, right?! Well, that can be bad, but you needn’t feel bad.
;;
;;; This quick demo will show you how seq chunking can bite, and how to avoid it.
;;;
;;; ## Contents
;;;
;;; 1. [What are seqs?](/worksheet.html?filename=src/den_of_clojure/sec1.clj)
;;; 2. [So what about lazy seqs?](/worksheet.html?filename=src/den_of_clojure/sec2.clj)
;;; 3. [...and chunked seqs?](/worksheet.html?filename=src/den_of_clojure/sec3.clj)
;;; 4. [Identifying chunked seqs](/worksheet.html?filename=src/den_of_clojure/sec4.clj)
;;; 5. [Unchunking](/worksheet.html?filename=src/den_of_clojure/sec5.clj)
;;; 6. [Be eager, not lazy (or, The Way Of The Reduce)](/worksheet.html?filename=src/den_of_clojure/sec6.clj)
;;; 7. [Reducers (maybe)](/worksheet.html?filename=src/den_of_clojure/sec7.clj)
;;; 8. [<strike>Transducers (next time perhaps)</strike>](/worksheet.html?filename=src/den_of_clojure/sec8.clj)
;; **

;; **
;;; ---
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec1.clj)
;; **
