;; gorilla-repl.fileformat = 1

;; **
;;; # 3. ...AND CHUNKED SEQS?
;; **

;; @@
(ns den-of-clojure.chunking)
;; @@

;; **
;;; So we know that lazy evaluation means that side effects only occur when the results of a lazy calculation are realized.
;;;
;;; With that in mind, how many times will this print to the console?
;; **

;; @@
(first (map println (range 100)))
;; @@

;; **
;;; It would be worse if, instead of just println, the function
;;; actually did something with a more substantive side effect.
;;;
;;; In this example, the side effect is a very expensive database query
;;; *(well, kinda...)*, but one necessary to determine whether to use the result.
;; **

;; @@
(def starting-values (vec (range 0 100)))

(defn lookup-item [n]
  (let [query-cost (* n n)]
    (println "Looking up" n)
    (Thread/sleep query-cost)
    {:query-cost query-cost}))

(defn valid-result? [result]
  (< (:query-cost result) 70))

(defn use-valid-results [results]
  (println "Results:" results))
;; @@

;; @@
(use-valid-results
 (take-while valid-result?
             (map lookup-item starting-values)))
;; @@

;; **
;;; What's good about chunked seqs?
;;; - optimization, to reduce the amount of machinery invoked to
;;;  realize an item in a seq
;;;
;;; What's bad about chunked seqs?
;;; - See above - you can easily end up realizing more in the lazy seq
;;;   than you realize. Need to be aware that this can happen,
;;;   sometimes even if you're careful to avoid it.
;; **
