library(tidyverse)
library(tidymodels)
library(caret)
library(SSLR)
library(doParallel)
library(MLmetrics)

set.seed(1)
dataset1 <- read.csv("...1. Preprocessing\\Output\\sub1_valence_3_clases.csv")
dataset2 <- read.csv("...1. Preprocessing\\Output\\sub2_valence_3_clases.csv")
dataset3 <- read.csv("...1. Preprocessing\\Output\\sub3_valence_3_clases.csv")
dataset4 <- read.csv("...1. Preprocessing\\Output\\sub4_valence_3_clases.csv")
dataset5 <- read.csv("...1. Preprocessing\\Output\\sub5_valence_3_clases.csv")
dataset6 <- read.csv("...1. Preprocessing\\Output\\sub6_valence_3_clases.csv")
dataset7 <- read.csv("...1. Preprocessing\\Output\\sub7_valence_3_clases.csv")
dataset8 <- read.csv("...1. Preprocessing\\Output\\sub8_valence_3_clases.csv")
dataset9 <- read.csv("...1. Preprocessing\\Output\\sub9_valence_3_clases.csv")
dataset10 <- read.csv("...1. Preprocessing\\Output\\sub10_valence_3_clases.csv")
dataset11 <- read.csv("...1. Preprocessing\\Output\\sub11_valence_3_clases.csv")
dataset12 <- read.csv("...1. Preprocessing\\Output\\sub12_valence_3_clases.csv")
dataset13 <- read.csv("...1. Preprocessing\\Output\\sub13_valence_3_clases.csv")
dataset14 <- read.csv("...1. Preprocessing\\Output\\sub14_valence_3_clases.csv")
dataset15 <- read.csv("...1. Preprocessing\\Output\\sub15_valence_3_clases.csv")
dataset16 <- read.csv("...1. Preprocessing\\Output\\sub16_valence_3_clases.csv")
dataset17 <- read.csv("...1. Preprocessing\\Output\\sub17_valence_3_clases.csv")
dataset18 <- read.csv("...1. Preprocessing\\Output\\sub18_valence_3_clases.csv")
dataset19 <- read.csv("...1. Preprocessing\\Output\\sub19_valence_3_clases.csv")
dataset20 <- read.csv("...1. Preprocessing\\Output\\sub20_valence_3_clases.csv")
dataset21 <- read.csv("...1. Preprocessing\\Output\\sub21_valence_3_clases.csv")
dataset22 <- read.csv("...1. Preprocessing\\Output\\sub22_valence_3_clases.csv")
dataset23 <- read.csv("...1. Preprocessing\\Output\\sub23_valence_3_clases.csv")
dataset24 <- read.csv("...1. Preprocessing\\Output\\sub24_valence_3_clases.csv")
dataset25 <- read.csv("...1. Preprocessing\\Output\\sub25_valence_3_clases.csv")
dataset26 <- read.csv("...1. Preprocessing\\Output\\sub26_valence_3_clases.csv")
dataset27 <- read.csv("...1. Preprocessing\\Output\\sub27_valence_3_clases.csv")
dataset28 <- read.csv("...1. Preprocessing\\Output\\sub28_valence_3_clases.csv")
dataset29 <- read.csv("...1. Preprocessing\\Output\\sub29_valence_3_clases.csv")
dataset30 <- read.csv("...1. Preprocessing\\Output\\sub30_valence_3_clases.csv")
dataset1 <- dataset1[sample(1:nrow(dataset1)),]
dataset2 <- dataset2[sample(1:nrow(dataset2)),]
dataset3 <- dataset3[sample(1:nrow(dataset3)),]
dataset4 <- dataset4[sample(1:nrow(dataset4)),]
dataset5 <- dataset5[sample(1:nrow(dataset5)),]
dataset6 <- dataset6[sample(1:nrow(dataset6)),]
dataset7 <- dataset7[sample(1:nrow(dataset7)),]
dataset8 <- dataset8[sample(1:nrow(dataset8)),]
dataset9 <- dataset9[sample(1:nrow(dataset9)),]
dataset10 <- dataset10[sample(1:nrow(dataset10)),]
dataset11 <- dataset11[sample(1:nrow(dataset11)),]
dataset12 <- dataset12[sample(1:nrow(dataset12)),]
dataset13 <- dataset13[sample(1:nrow(dataset13)),]
dataset14 <- dataset14[sample(1:nrow(dataset14)),]
dataset15 <- dataset15[sample(1:nrow(dataset15)),]
dataset16 <- dataset16[sample(1:nrow(dataset16)),]
dataset17 <- dataset17[sample(1:nrow(dataset17)),]
dataset18 <- dataset18[sample(1:nrow(dataset18)),]
dataset19 <- dataset19[sample(1:nrow(dataset19)),]
dataset20 <- dataset20[sample(1:nrow(dataset20)),]
dataset21 <- dataset21[sample(1:nrow(dataset21)),]
dataset22 <- dataset22[sample(1:nrow(dataset22)),]
dataset23 <- dataset23[sample(1:nrow(dataset23)),]
dataset24 <- dataset24[sample(1:nrow(dataset24)),]
dataset25 <- dataset25[sample(1:nrow(dataset25)),]
dataset26 <- dataset26[sample(1:nrow(dataset26)),]
dataset27 <- dataset27[sample(1:nrow(dataset27)),]
dataset28 <- dataset28[sample(1:nrow(dataset28)),]
dataset29 <- dataset29[sample(1:nrow(dataset29)),]
dataset30 <- dataset30[sample(1:nrow(dataset30)),]
confusionMatrix_list1<-list()
confusionMatrix_list2<-list()
confusionMatrix_list3<-list()
confusionMatrix_list4<-list()
confusionMatrix_list5<-list()
confusionMatrix_list6<-list()
confusionMatrix_list7<-list()
confusionMatrix_list8<-list()
confusionMatrix_list9<-list()
confusionMatrix_list10<-list()
confusionMatrix_list11<-list()
confusionMatrix_list12<-list()
confusionMatrix_list13<-list()
confusionMatrix_list14<-list()
confusionMatrix_list15<-list()
confusionMatrix_list16<-list()
confusionMatrix_list17<-list()
confusionMatrix_list18<-list()
confusionMatrix_list19<-list()
confusionMatrix_list20<-list()
confusionMatrix_list21<-list()
confusionMatrix_list22<-list()
confusionMatrix_list23<-list()
confusionMatrix_list24<-list()
confusionMatrix_list25<-list()
confusionMatrix_list26<-list()
confusionMatrix_list27<-list()
confusionMatrix_list28<-list()
confusionMatrix_list29<-list()
confusionMatrix_list30<-list()

for (i in 1:30){
dataset <- split(eval(parse(text = paste("dataset", i,sep = ""))), rep(1:10, length.out = nrow(eval(parse(text = paste("dataset", i,sep = "")))), each = ceiling(nrow(eval(parse(text = paste("dataset", i,sep = ""))))/10)))  
for (j in 1:10){
array_num <- 1:10
array_num <- array_num[- j]    
test <- eval(parse(text = paste("dataset$'", j,"'",sep = "")))  
train=rbind(eval(parse(text = paste("dataset$'", array_num[1],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[2],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[3],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[4],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[5],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[6],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[7],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[8],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[9],"'",sep = ""))))
train <- transform(train, valence = as.factor(valence))
cls <- which(colnames(train) == "valence")
labeled.index <- createDataPartition(train$valence, p = .2, list = FALSE)
train[-labeled.index,cls] <- NA
cl <- makePSOCKcluster(10)
registerDoParallel(cl)
rf <- rand_forest(trees = 100, mode = "classification") %>%
  set_engine("randomForest")
m <- coBC(learner = rf, max.iter=3) %>% fit(valence ~ ., data = train)
stopCluster(cl)
predictions=predict(m,test)%>%bind_cols(test)
data=predictions$.pred_class
data <- factor(data, levels = c('1', '2','3'))
reference = factor(predictions$valence)
reference <- factor(reference, levels = c('1', '2','3'))
confusionMatrix <- confusionMatrix(data=data, reference=reference,mode = "prec_recall")
print(i)
print(j)
print(confusionMatrix)
assign(paste("confusionMatrix_list", i,sep = ""),append(eval(parse(text = paste("confusionMatrix_list", i,sep = ""))),list(confusionMatrix)))
}
}
