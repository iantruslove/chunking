;; gorilla-repl.fileformat = 1

;; **
;;; # PUZZLE TIME
;;;
;;; Find a friend, go solo in the corner, ask for help, shout out good
;;; ideas, cheating is the new winning!
;; **

;; @@
(ns den-of-clojure.puzzle
  (:require [clojure.test :refer :all]))
;; @@

;; **
;;; ## MAP/REDUCE
;;; Given what you've learned, re-implement clojure's `map` function
;;; but make it operate eagerly, and use reduce to drive any evaluations.
;; **

;; @@
(defn my-map [f coll]
  ;; TODO: stuff needs to go here!
  )
;; @@

;; **
;;; Some trivial verification to get you going:
;; **

;; @@
(is (= '(1 2 3) (my-map identity '(1 2 3))))
(is (= '(1 2 3) (my-map identity [1 2 3])))
(is (= '(1 2 3) (my-map inc [0 1 2])))
;; @@

;; **
;;; ## BONUS POINTS
;;; Replace clojure's `map` function so that it operates highly
;;; parallelizedly.
;;; * What assumptions about side effects and ordering do you have to
;;;  impose on your function's callers?
;;;
;; **

;; @@
(defn my-parallel-map [f coll]
  ;; ?!
  )
;; @@
