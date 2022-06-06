# ZIO Tech challenge

In this [folder](https://drive.google.com/drive/folders/1W2KaIm7zJIf5pGBf3uGrUARyV2q9rMgA)
you will find binaries for Mac/Windows/Linux - these binaries will
print out JSON lines with attributes "event_type", "data", and
"timestamp". Event_type and data are strings, timestamps are seconds
since the Epoch and increase monotonically.

Your challenge:
* Create an application that reads the lines emitted by the process and performs a windowed word count, grouped by event_type (sliding window with an arbitrary duration left for you to choose).
* The word count should be the frequency per word per event type.
* The current word count should be exposed over an HTTP interface from your application.
  * Note that the binaries sometimes output garbage data, so you’ll need to handle that gracefully.
  * The application should be written in Scala and use the hunting-pipeline tech stack.
* Create a Github repository with a solution
* Please write code that you are proud of, but keep the effort reasonable. We do not need a full production system.

> Because the Mac OS binary is for Intel-based Macs, I couldn't execute it on my M1 Pro. 
> I simulate streaming by reading from dump file (blackbox.out) and pausing for 1 second between emitting lines  