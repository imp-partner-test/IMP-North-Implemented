#! /cygdrive/d/tools/groovy-2.1.6/bin/groovy

import groovy.json.*

def projName = "IMP North. Implemented & Dredd testable"
def dirName = "IMP-North-Implemented-DreddTestable"
def host = "https://cluster.production.imp.mobilitaetsdienste.de:8443"
println ("************************************")
println ("generating dredd testable project: " + projName)
println ("************************************")
  
// 1. the base apiary
//def baseFolder = "IMP-North-Implemented"
//def apiBaseFileName = "apiaryOrig.apib"					// local
//def apiBaseFileName = baseFolder + "/apiary.apib"
def apiBaseFileName = "apiary.apib"
println "the base file name: " + apiBaseFileName
def fileText = new File(apiBaseFileName).getText()		

// 2. the generated apiary
//def apiFileName = "apiary.apib"							// local
def apiFileName = dirName + "/apiary.apib"			// on Jenkins
println "the file to be generated: " + apiFileName

//def bakFileName = apiFileName + ".bak"
//def bakFile = new File(bakFileName)
//bakFile.delete()
def apiFile = new File(apiFileName)
//apiFile.renameTo(bakFileName)

apiFile.append("FORMAT: 1A\n");
apiFile.append("HOST: " + host + "\n\n")

apiFile.append("# " + projName + "\n")
apiFile.append(findProjIntro(fileText))


def allGroupNames = findAllGroupNames(fileText)

for (int i = 0; i < allGroupNames.size; i++) {
	def nextGroupName = allGroupNames[i]
	def groupText = findGroupText(nextGroupName, fileText)
	def testable = '@testable'	
	if (groupText != null && groupText.contains(testable))
	{
		def groupIntroWithPath = findGroupIntoWithPath(groupText)
		def allCollectionTexts = findAllCollectionTexts(groupText)
		
		println("------ " + "GroupName " + nextGroupName + " ------")
		
		apiFile.append("\n") // additional new line
		apiFile.append(groupIntroWithPath)
		println("groupIntroWithPath inserted ")
		
		for (int k = 0; k < allCollectionTexts.size; k++) {
			def nextCollectionText = allCollectionTexts[k]
			def collectionIntroWithPath = findCollectionIntroWithPath(nextCollectionText)
			def testableResources = findAllTestableResources(allCollectionTexts[k])
			
			if (testableResources.size > 0)
			{
				apiFile.append("\n") // additional new line
				apiFile.append(collectionIntroWithPath)
				println("collectionIntroWithPath inserted")
				
				for (int j = 0; j < testableResources.size; j++)
				{
					apiFile.append("\n") // additional new line
					apiFile.append(testableResources[j])
				}
				println("Number of testable resources " + testableResources.size)	
				
			}	
		}	
	}
}

def findAllGroupNames(fileText){
	//def text = new File("apiaryOrig.apib").getText() //!
	def group = "# Group "
	def groupNameList = fileText.findAll(/$group(?s).*?(?=\n)/) //matching anything until = (?s).*?(until here)
	return groupNameList
}

//######################### looking for introductions of sertain sections

def findProjIntro(fileText){
	def projIntroWithOldName = fileText.find(/\n# (?s).*?(?=\n#)/) // matching anything until = (?s).*?(?= until this BUT NOT including it)
	def projOldName = projIntroWithOldName.find(/\n# (?s).*?(\n)/)
	def projIntro = projIntroWithOldName.replace(projOldName, "")
	return projIntro
}

def findGroupIntoWithPath(groupText){
	def introWithPath = groupText.find(/\n# (?s).*?(?=\n##)/) // matching anything until = (?s).*?(?= until this BUT NOT including it)
	return introWithPath
}

def findCollectionIntroWithPath(nextCollectionText){
	def introWithPath = nextCollectionText.find(/\n## (?s).*?(?=\n###)/)
	return introWithPath
}

//######################### looking for texts of sertain sections

//----------------------- Group ends by next group or end of file
def findGroupText(groupName, fileText){
	//println("* findChapter for the groupName " + groupName);
	//def text = new File("apiaryOrig.apib").getText()	//!
	def group = "# Group "
	def chapText = fileText.find(/\n$groupName\s*\n(?s).*?(?=\n# Group |$)/) //matching but not (?=this)
	return chapText
}

//----------------------- Collection ends by next collection, next group or end of file
def findAllCollectionTexts(groupText){
	def collectionTexts = groupText.findAll(/\n## (?s).*?(?=\n## |\n# |$)/) // matching anything until = (?s).*?(?= until this BUT NOT including it)
	return collectionTexts
}

//----------------------- Resource ends by next resource, next collection, next group or end of file
def  findAllTestableResources(collectionText){
  //println("* findAllTestableResources");
  def testable = '@testable'
  def result = ''
  def allRessourcesList = collectionText.findAll(/\n###(?s).*?(?=\n### |\n## |\n# |$)/)
  def allTestableRessourcesList = []
  for (int i = 0; i < allRessourcesList.size; i++) {
	  def resource = allRessourcesList[i]
	  if (resource.contains(testable))
	  {
	  	allTestableRessourcesList.add(resource)
	  }			
  }  
  return allTestableRessourcesList
}
