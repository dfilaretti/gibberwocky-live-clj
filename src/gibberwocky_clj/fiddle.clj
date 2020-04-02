(ns gibberwocky-clj.fiddle
  (:require
    [gibberwocky-clj.core :as core]
    [gibberwocky-clj.connection :as connection]
    [gibberwocky-clj.message.handler :as handler]
    [gibberwocky-clj.lom :as lom]
    [gibberwocky-clj.composition :as composition]
    [manifold.stream :as s]))

(comment
  ;; let's connect to the Gibberworky server (M4L device)
  (def c @(connection/open))
  ;; and set a couple of things up things up...
  (handler/setup c)
  ;; current LOM (live Object Model) will not be unintialised
  @lom/lom
  ;; Let's sent the get_scene message to GibberWocky.
  ;; It will cause it to send us a new LOM representation
  @(s/put! c "get_scene")
  ;; Check the LOM again and it will be filled up with stuff...
  @lom/lom
  ;; We can now explore the LOM...
  ;; e.g. let's look at available tracks (ID and name)
  (lom/->tracks)
  ;; Let's use the ID to play a note (simple)
  (s/put! c "19 note 72")
  ;; let's now add some message to our pool
  (composition/add-events!
    [{:track-id 5 :beat 1 :pitch 60 :velocity 64 :length 50}
     {:track-id 5 :beat 2 :pitch 60 :velocity 64 :length 50}
     {:track-id 5 :beat 3 :pitch 60 :velocity 64 :length 50}
     {:track-id 5 :beat 4 :pitch 60 :velocity 64 :length 50}
     {:track-id 5 :beat 2 :pitch 62 :velocity 64 :length 50}
     {:track-id 5 :beat 4 :pitch 62 :velocity 64 :length 50}
     {:track-id 5 :beat 1.5 :pitch 60 :velocity 40 :length 50}
     {:track-id 5 :beat 1.75 :pitch 60 :velocity 30 :length 50}
     {:track-id 596 :beat 3 :pitch 44 :velocity 64 :length 50}
     {:track-id 19 :beat 4 :pitch 67 :velocity 64 :length 50}
     {:track-id 19 :beat 4.5 :pitch 60 :velocity 62 :length 50}])
  ;; get msgs in the pool for beat 1 (as example)
  (composition/remove-all-events!)
  ;; or select different tracks in the live set...
  (s/put! c "select_track 19")
  (s/put! c "select_track 5")
  ;; And finally we close the connection...
  (connection/close c)
  ;; This shouldn't work anymore since connection is closed
  (s/put! c "5 note 62"))
