(ns net.cassiel.reveal-bb.render
  (:require [babashka.pods :as pods]
            [babashka.fs :as fs])
  (:import [java.io File]))

(pods/load-pod 'retrogradeorbit/bootleg "0.1.9")
(require '[pod.retrogradeorbit.bootleg.utils :as utils])

(defn htmlize [text]
  (-> text
      (clojure.string/replace "&" "&amp;")
      (clojure.string/replace "<" "&lt;")
      (clojure.string/replace ">" "&gt;")))

(defn tt [text]
  [:span {:style (->> ["font-family: monospace"
                       "font-size: 0.8em"
                       "color: #A0A0FF"
                       #_ "background-color:rgba(150, 150, 150, 0.5)"
                       "padding: 0px 5px"
                       ""]
                      (clojure.string/join ";"))}
   (htmlize text)])

(defn image-h [h f]
  [:img {:height h
         :style "margin:10px; vertical-align:middle"
         :src (->> f
                   (File. "images")
                   #_ (File. ASSET-ROOT-URL)
                   str)}])

(def image (partial image-h 480))

(defn- copy-reveal-js [reveal-location out-dir]
  (when (fs/exists? out-dir) (fs/delete-tree out-dir))
  (fs/create-dir out-dir)
  (doseq [f ["dist" "plugin"]]
    (fs/copy-tree (File. reveal-location f)
                  (File. out-dir f))))

(defn render [& {:keys [theme title author slides reveal-location out-dir]}]
  (let [template (clojure.java.io/resource "template.html")
        template-html (slurp template)
        content (utils/as-html slides)
        all-html (-> template-html
                     (clojure.string/replace "__TITLE__" title)
                     (clojure.string/replace "__AUTHOR__" author)
                     (clojure.string/replace "__THEME__" (name theme))
                     (clojure.string/replace "__CONTENT__" content))
        out-dir (File. out-dir "_OUTPUT")]
    (copy-reveal-js reveal-location out-dir)
    (when (fs/exists? "images") (fs/copy-tree "images" (File. out-dir "images")))
    (spit (File. out-dir "index.html") all-html)))
