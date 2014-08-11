#! /cygdrive/d/tools/groovy-2.1.6/bin/groovy

import groovy.json.*

def slurper = new JsonSlurper()
def ctlJson = new File("project-control.json").getText("UTF-8")

def ctl = slurper.parseText(ctlJson)

println ("generating apiary projects")

//def baseFolder = "IMP-North-Implemented/"
def baseFolder = ""
def baseApiFileText = new File(baseFolder + "apiary.apib").getText() // the base apib file

ctl.Projects.each{ 
  println "project: " + it.Project
  
  def dirName = it.Project
  def apiFileName = dirName + "/apiary.apib" // the apib file to be generated
  println "apiFileName: " + apiFileName
  def bakFileName = apiFileName + ".bak"
  def bakFile = new File(bakFileName)
  bakFile.delete()
  def apiFile = new File(apiFileName)
  apiFile.renameTo(bakFileName)

  apiFile.append("FORMAT: 1A\n");
  apiFile.append("HOST: " + it.Host + "\n\n")
  println("host: " + it.Host +  "\n")
  apiFile.append("# " + it.Title + " \n")
  
  apiFile.append(findProjIntro(baseApiFileText))
  
  
  it.Chapters.each {
	  apiFile.append(findChapter(it, baseApiFileText))
	  }
}



def findChapter(chapName, baseApiFileText){
  println("* chapter: " + chapName);
  //def text = new File("IMP-North-Implemented/apiary.apib").getText() oldddddddddd
  //def text = new File(baseFolder + "apiary.apib").getText()
  
  assert '\n#  ' + chapName + '  	\n \n' ==~ /\n#\s*$chapName\s*\n/
  
  //NOTE: Introduction has "# " before the chapter name
  if (chapName ==~ /Introduction/) 
  {
	  // matches # Introduction blabla \n blabla \n# bebe
	  def chap = baseApiFileText.find(/\n#\s*$chapName\s*\n(?s).*?(?=\n#\s*)/) //matching anything but not (?=this)
	  // def chap = text.find(/\-\-\s*\n$chapName\s*\n(?s).*?(\n\-\-\s*\n)/) //matching anything until = (?s).*?(until here) oldddddddddddddddddddd
	  //println("* chap:\r\n" + chap)
	  assert chap != null
	  return chap
  }
  // NOTE: All other chapters have "# Group " before the chapter name
  else 
  {
	  //matches \n# Group  Chaptername \n bla bla till next \n# Group Chaptername
	  def chap = baseApiFileText.find(/\n#\s*Group\s*$chapName\s*\n(?s).*?(?=\n#\s*Group\s*)/) //matching but not (?=this)
	  //println("* chap:\r\n" + chap)
	  assert chap != null
	  return chap
  }  
}

// looking for proj intro

def findProjIntro(fileText){
	def projIntroWithOldName = fileText.find(/\n# (?s).*?(?=\n#)/) // matching anything until = (?s).*?(?= until this BUT NOT including it)
	def projOldName = projIntroWithOldName.find(/\n# (?s).*?(\n)/)
	def projIntro = projIntroWithOldName.replace(projOldName, "")
	return projIntro
}
