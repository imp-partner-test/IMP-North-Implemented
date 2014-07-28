#! /cygdrive/d/tools/groovy-2.1.6/bin/groovy

import groovy.json.*

def slurper = new JsonSlurper()
def ctlJson = new File("project-control.json").getText("UTF-8")

def ctl = slurper.parseText(ctlJson)

println ("generating apiary projects")

ctl.Projects.each{ 
  println "project: " + it.Project
  
  def dirName = it.Project
  def apiFileName = dirName + "/apiary.apib"
  println "apiFileName: " + apiFileName
  def bakFileName = apiFileName + ".bak"
  def bakFile = new File(bakFileName)
  bakFile.delete()
  def apiFile = new File(apiFileName)
  apiFile.renameTo(bakFileName)
  apiFile.append("HOST: " + it.Host + "\n\n")
  apiFile.append("--- " + it.Title + " ---\n")
  
  it.Chapters.each {
	  apiFile.append(findChapter(it))
	  }
}


def findChapter(chapName){
  println("* chapter: " + chapName);
  def text = new File("IMP-North-Implemented/apiary.apib").getText()
  
  assert '--\n' + chapName + '  	\n-- \n' ==~ /\-\-\s*\n$chapName\s*\n\-\-\s*\n/
  
  //NOTE: Introcuction has "--" ONLY before the chapter name bot not after the chapter name
  if (chapName ==~ /Introduction/) 
  {
	  // matches -- Introduction blabla --
	  def chap = text.find(/\-\-\s*\n$chapName\s*\n(?s).*?(\n\-\-\s*\n)/) //matching anything until = (?s).*?(until here)
	  //println("* chap:\r\n" + chap)
	  assert chap != null
	  return chap
  }
  // NOTE: All other chapters have "--" before the chapter name and "--" after the chapter name
  else 
  {
	  //matches -- Chapter -- blabla till next -- Chapter --
	  def chap = text.find(/\-\-\s*\n$chapName\s*\n(?s).*?(?=\n\-\-\s*\n)/) //matching but not (?=this)
	  //println("* chap:\r\n" + chap)
	  assert '--\n' + chapName + '  	\n-- \n' ==~ /\-\-\s*\n$chapName\s*\n\-\-\s*\n/
	  assert chap != null
	  return chap
  }  
}

