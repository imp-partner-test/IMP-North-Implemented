#! /cygdrive/d/tools/groovy-2.1.6/bin/groovy

import groovy.json.*

def slurper = new JsonSlurper()
def ctlJson = new File("project-control.json").getText("UTF-8")

def ctl = slurper.parseText(ctlJson)

println ("generating apiary projects")
ctl.Projects.each{ 
  println "project: " + it.Project
  def dirName = it.Project
  def apiFileName = "../" + dirName + "/apiary.apib"
  def bakFileName = apiFileName + ".bak"
  def bakFile = new File(bakFileName)
  bakFile.delete()
  def apiFile = new File(apiFileName)
  apiFile.renameTo(bakFileName)
  apiFile.append("HOST: " + it.Host + "\n\n")
  apiFile.append("--- " + it.Title + " ---\n")
  it.Chapters.each {apiFile.append(findChapter(it))}
}

def findChapter(chapName){
  println("* chapter: " + chapName);
  def text = new File("./apiary.apib").getText()
  def chap = text.find(/(?s)\n\-\-\b?\n$chapName\n\-\-\n.*?\-\-\n/)
  chap.minus( ~/(?s)\-\-\n$/ )
}

