(require '[babashka.fs :as fs])

(fs/read-all-lines "/Users/nick/GITHUB/cassiel/reveal.js/index.html")

(slurp "/Users/nick/GITHUB/cassiel/reveal.js/index.html")

(System/getProperty "home")


(-> (slurp "/Users/nick/GITHUB/cassiel/reveal.js/index.html")
    (utils/convert-to :html)
    )

(require '[sci.core :as cli])

(sci/eval-string "(inc 1)")

(import 'java.io.File)

(java.io.File. (java.io.File. "A") "B")

(require '[net.cassiel.reveal-bb.render :as r])

r/render

(-> (System/getProperty "user.home")
    (File. "GITHUB")
    (File. "cassiel")
    (File. "reveal.js")
    str)

(File.
 (System/getProperty "user.home")
 "X")

(fs/cwd)

(-> "AXA"
    (clojure.string/replace "X" "Y")
    (clojure.string/replace "A" "B")
    )

(slurp "template.html")