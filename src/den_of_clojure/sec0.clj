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
;;; 1. What are seqs?
;;; 2. So what about lazy seqs?
;;; 3. ...and chunked seqs?
;;; 4. Identifying chunked seqs
;;; 5. Unchunking
;;; 6. Be eager, not lazy (or, The Way Of The Reduce)
;;; 7. Reducers (maybe)
;;; 8. Transducers (maybe maybe)
;; **

;; **
;;; ---
;;; [next](/worksheet.html?filename=src/den_of_clojure/sec1.clj)
;; **
