(ns net.cassiel.reveal-bb.watcher
  (:require [babashka.fs :as fs]
            [pod.babashka.fswatcher :as fw]
            [clojure.java.shell :refer [sh]]))

(defn watch
  "Run watcher on a Clojure file of slides.
  `reveal-location` is the root of our checked-out `reveal.js`, `ssi-version` branch."
  [reveal-location clj-file]
  (letfn [(process []
            (println (str (java.util.Date.)) "--" clj-file)
            (let [output (sh "bb" clj-file :env {:INPUT_LOCATION (-> (fs/absolutize clj-file) (fs/parent))
                                                 :INPUT_FILE (fs/file-name clj-file)
                                                 :REVEAL_LOCATION reveal-location})]
              (println "OUT:")
              (println (:out output))
              (print "ERR:" (:err output))
              (println "DONE")))]
    (process)
    (fw/watch clj-file
              (fn [event]
                ;; (println event)
                (when (#{:write :write|chmod} (:type event))
                  (process)))
              {:delay-ms 500})))

(defn watch-all [reveal-location filespec]
  (doseq [path (fs/glob "." filespec)
          :let [name (str path)]]
    (if (re-matches #".*\.clj$" name)
      (watch reveal-location name)
      (println "ignoring" name)))
  (deref (promise)))
