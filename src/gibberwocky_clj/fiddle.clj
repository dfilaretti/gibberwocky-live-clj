(ns gibberwocky-clj.fiddle
  (:require
    [gibberwocky-clj.core :as core]
    [gibberwocky-clj.connection :as connection]
    [gibberwocky-clj.message.handler :as handler]
    [gibberwocky-clj.lom :as lom]
    [gibberwocky-clj.composition :as composition]
    [gibberwocky-clj.pattern :as pattern]
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
  ;; We can now query the LOM...
  ;; e.g. let's look at available tracks (ID and name):
  (lom/->tracks)
  ;; We can send raw commands to Gibberwocky e.g.
  (s/put! c "19 note 72")
  (s/put! c "19 note 62")
  (s/put! c "select_track 19")
  (s/put! c "select_track 5")
  ;; But that's not very fun. Let's try to play some pattern instead!
  ;; A *pattern* is just a collection of *events*.
  ;; We can create one manually:
  (composition/replace-pattern!
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
  ;; But we can also use helpers to generate patterns...
  (composition/replace-pattern!
    (pattern/simple-seq
      5  ;; track id
      60 62 60 60 62 60 60 60
      60 62 60 60 62 60 62 62))
  ;; and again...
  (composition/replace-pattern!
    (pattern/simple-seq
      19  ;; track id
      40 50 60 70))
  ;; We can clear the pattern...
  (composition/clear-pattern!)
  ;; And finally we close the connection...
  (connection/close c)
  ;; This shouldn't work anymore since connection is closed
  (s/put! c "5 note 62"))
