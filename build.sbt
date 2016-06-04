import java.io.{BufferedWriter, FileWriter}

import scala.io.Source

name := "WebGazerScala"

scalaVersion in ThisBuild := "2.11.7"

enablePlugins(ScalaJSPlugin)
enablePlugins(SbtWeb)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.0",
  "com.greencatsoft" %%% "scalajs-angular" % "0.6",
  "biz.enef" %%% "slogging" % "0.4.0"
)


jsDependencies ++= Seq(
  "org.webjars" % "angularjs" % "1.3.15" / "angular.js",
  "org.webjars" % "angularjs" % "1.3.15" / "angular-route.js" dependsOn "angular.js",
  "org.webjars" % "angularjs" % "1.3.15" / "angular-animate.js" dependsOn "angular.js",
  "org.webjars" % "angularjs" % "1.3.15" / "angular-aria.js" dependsOn "angular.js",
  "org.webjars" % "angularjs" % "1.3.15" / "angular-locale_ko.js" dependsOn "angular.js"
)


//  RuntimeDOM)

//Settings WorkBench

workbenchSettings

bootSnippet := "nl.quintor.dstibbe.todo.MainApp().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)


commands += Command.command("concat")((state: State) => {
  println("Concatenating ... ")
  val files = Seq( 
    "./dependencies/js-objectdetect/js/objectdetect.js",
    "./dependencies/js-objectdetect/js/objectdetect.eye.js",
    "./dependencies/js-objectdetect/js/objectdetect.frontalface_alt.js",
    "./dependencies/tracking.js/build/tracking.js",
    "./dependencies/tracking.js/build/data/face-min.js",
    "./dependencies/tracking.js/build/data/eye-min.js",
    "./dependencies/clmtrackr/utils.js",
    "./dependencies/numeric-1.2.6.min.js",
    "./dependencies/clmtrackr/mosse.js",
    "./dependencies/clmtrackr/jsfeat-min.js",
    "./dependencies/clmtrackr/frontalface.js",
    "./dependencies/clmtrackr/jsfeat_detect.js",
    "./dependencies/clmtrackr/left_eye_filter.js",
    "./dependencies/clmtrackr/right_eye_filter.js",
    "./dependencies/clmtrackr/nose_filter.js",
    "./dependencies/clmtrackr/model_pca_20_svm.js",
    "./dependencies/clmtrackr/clm.js",
    "./dependencies/clmtrackr/svmfilter_webgl.js",
    "./dependencies/clmtrackr/svmfilter_fft.js",
    "./dependencies/clmtrackr/mossefilter.js",
    "./src/main/resources/js/blinkDetector.js",
    "./src/main/resources/js/clmGaze.js",
    "./src/main/resources/js/trackingjsGaze.js",
    "./src/main/resources/js/js_objectdetectGaze.js",
    "./src/main/resources/js/linearReg.js",
    "./src/main/resources/js/mat.js",
    "./src/main/resources/js/pupil.js",
    "./src/main/resources/js/regression.js",
    "./src/main/resources/js/ridgeReg.js",
    "./src/main/resources/js/ridgeWeightedReg.js",
    "./src/main/resources/js/util.js",
    "./src/main/resources/js/webgazer.js")

  val dir = new File("./target/scala-2.11/classes/js/")
  dir.mkdirs()
  
  val writer = new BufferedWriter(new FileWriter( new File( dir , "webgazer-complete.js")))
  for( fileName <- files ) {
    writer.write("// filename: " + fileName + "\n")

    println("Add file: " + fileName)

      Source.fromFile(fileName).getLines().foreach(line => {
        writer.write(line + "\n")
      })

    writer.flush()
  }
  writer.close()


  println("Concatenating ... done")

  state

})

commands += Command.command("runme")((state: State) => {
  println("runme ... ")

  var newState = state

  newState = Command.process("fastOptJS", newState)
  newState = Command.process("concat", newState)

  newState
})
