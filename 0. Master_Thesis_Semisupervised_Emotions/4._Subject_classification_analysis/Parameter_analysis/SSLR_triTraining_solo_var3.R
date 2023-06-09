library(tidyverse)
library(tidymodels)
library(caret)
library(SSLR)
library(doParallel)
library(MLmetrics)

dataset1 <- read.csv("...1. Preprocessing\\Output\\sub1_arousal_2_clases.csv")
dataset1<-dataset1[-801]
dataset1$subject<-1
dataset2 <- read.csv("...1. Preprocessing\\Output\\sub2_arousal_2_clases.csv")
dataset2<-dataset2[-801]
dataset2$subject<-2
dataset3 <- read.csv("...1. Preprocessing\\Output\\sub3_arousal_2_clases.csv")
dataset3<-dataset3[-801]
dataset3$subject<-3
dataset4 <- read.csv("...1. Preprocessing\\Output\\sub4_arousal_2_clases.csv")
dataset4<-dataset4[-801]
dataset4$subject<-4
dataset5 <- read.csv("...1. Preprocessing\\Output\\sub5_arousal_2_clases.csv")
dataset5<-dataset5[-801]
dataset5$subject<-5
dataset6 <- read.csv("...1. Preprocessing\\Output\\sub6_arousal_2_clases.csv")
dataset6<-dataset6[-801]
dataset6$subject<-6
dataset7 <- read.csv("...1. Preprocessing\\Output\\sub7_arousal_2_clases.csv")
dataset7<-dataset7[-801]
dataset7$subject<-7
dataset8 <- read.csv("...1. Preprocessing\\Output\\sub8_arousal_2_clases.csv")
dataset8<-dataset8[-801]
dataset8$subject<-8
dataset9 <- read.csv("...1. Preprocessing\\Output\\sub9_arousal_2_clases.csv")
dataset9<-dataset9[-801]
dataset9$subject<-9
dataset10 <- read.csv("...1. Preprocessing\\Output\\sub10_arousal_2_clases.csv")
dataset10<-dataset10[-801]
dataset10$subject<-10
dataset11 <- read.csv("...1. Preprocessing\\Output\\sub11_arousal_2_clases.csv")
dataset11<-dataset11[-801]
dataset11$subject<-11
dataset12 <- read.csv("...1. Preprocessing\\Output\\sub12_arousal_2_clases.csv")
dataset12<-dataset12[-801]
dataset12$subject<-12
dataset13 <- read.csv("...1. Preprocessing\\Output\\sub13_arousal_2_clases.csv")
dataset13<-dataset13[-801]
dataset13$subject<-13
dataset14 <- read.csv("...1. Preprocessing\\Output\\sub14_arousal_2_clases.csv")
dataset14<-dataset14[-801]
dataset14$subject<-14
dataset15 <- read.csv("...1. Preprocessing\\Output\\sub15_arousal_2_clases.csv")
dataset15<-dataset15[-801]
dataset15$subject<-15
dataset16 <- read.csv("...1. Preprocessing\\Output\\sub16_arousal_2_clases.csv")
dataset16<-dataset16[-801]
dataset16$subject<-16
dataset17 <- read.csv("...1. Preprocessing\\Output\\sub17_arousal_2_clases.csv")
dataset17<-dataset17[-801]
dataset17$subject<-17
dataset18 <- read.csv("...1. Preprocessing\\Output\\sub18_arousal_2_clases.csv")
dataset18<-dataset18[-801]
dataset18$subject<-18
dataset19 <- read.csv("...1. Preprocessing\\Output\\sub19_arousal_2_clases.csv")
dataset19<-dataset19[-801]
dataset19$subject<-19
dataset20 <- read.csv("...1. Preprocessing\\Output\\sub20_arousal_2_clases.csv")
dataset20<-dataset20[-801]
dataset20$subject<-20
dataset21 <- read.csv("...1. Preprocessing\\Output\\sub21_arousal_2_clases.csv")
dataset21<-dataset21[-801]
dataset21$subject<-21
dataset22 <- read.csv("...1. Preprocessing\\Output\\sub22_arousal_2_clases.csv")
dataset22<-dataset22[-801]
dataset22$subject<-22
dataset23 <- read.csv("...1. Preprocessing\\Output\\sub23_arousal_2_clases.csv")
dataset23<-dataset23[-801]
dataset23$subject<-23
dataset24 <- read.csv("...1. Preprocessing\\Output\\sub24_arousal_2_clases.csv")
dataset24<-dataset24[-801]
dataset24$subject<-24
dataset25 <- read.csv("...1. Preprocessing\\Output\\sub25_arousal_2_clases.csv")
dataset25<-dataset25[-801]
dataset25$subject<-25
dataset26 <- read.csv("...1. Preprocessing\\Output\\sub26_arousal_2_clases.csv")
dataset26<-dataset26[-801]
dataset26$subject<-26
dataset27 <- read.csv("...1. Preprocessing\\Output\\sub27_arousal_2_clases.csv")
dataset27<-dataset27[-801]
dataset27$subject<-27
dataset28 <- read.csv("...1. Preprocessing\\Output\\sub28_arousal_2_clases.csv")
dataset28<-dataset28[-801]
dataset28$subject<-28
dataset29 <- read.csv("...1. Preprocessing\\Output\\sub29_arousal_2_clases.csv")
dataset29<-dataset29[-801]
dataset29$subject<-29
dataset30 <- read.csv("...1. Preprocessing\\Output\\sub30_arousal_2_clases.csv")
dataset30<-dataset30[-801]
dataset30$subject<-30
confusionMatrix_list<-list()
set.seed(1)
dataset_tot<-rbind(dataset1,dataset2,dataset3,dataset4,dataset5,dataset6,dataset7,dataset8,dataset9,dataset10,dataset11,dataset12,dataset13,dataset14,dataset15,dataset16,dataset17,dataset18,dataset19,dataset20,dataset21,dataset22,dataset23,dataset24,dataset25,dataset26,dataset27,dataset28,dataset29,dataset30)
dataset_tot<-dataset_tot[sample(1:nrow(dataset_tot)),]
eliminados_1<-seq(1, 800, by=8)
for (elemento in eliminados_1){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}
eliminados_2<-seq(2, 800, by=8)
for (elemento in eliminados_2){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}
eliminados_4<-seq(4, 800, by=8)
for (elemento in eliminados_4){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}
eliminados_5<-seq(5, 800, by=8)
for (elemento in eliminados_5){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}
eliminados_6<-seq(6, 800, by=8)
for (elemento in eliminados_6){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}
eliminados_7<-seq(7, 800, by=8)
for (elemento in eliminados_7){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}
eliminados_8<-seq(8, 800, by=8)
for (elemento in eliminados_8){
  name_column<-paste(c("X", elemento), collapse = "")
  dataset_tot<-select(dataset_tot, -c(name_column))
}

dataset <- split(dataset_tot, rep(1:10, length.out = nrow(dataset_tot), each = ceiling(nrow(dataset_tot)/10)))

for (i in 1:10){ 
array_num <- 1:10
array_num <- array_num[- i]    
test <- eval(parse(text = paste("dataset$'", i,"'",sep = "")))  
train=rbind(eval(parse(text = paste("dataset$'", array_num[1],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[2],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[3],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[4],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[5],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[6],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[7],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[8],"'",sep = ""))),eval(parse(text = paste("dataset$'", array_num[9],"'",sep = ""))))
train <- transform(train, subject = as.factor(subject))
cls <- which(colnames(dataset_tot) == "subject")
labeled.index <- createDataPartition(train$subject, p = .2, list = FALSE)
train[-labeled.index,cls] <- NA
cl <- makePSOCKcluster(10)
registerDoParallel(cl)
rf <- rand_forest(trees = 100, mode = "classification") %>%
  set_engine("randomForest")
m <- triTraining(learner = rf) %>% fit(subject ~ ., data = train)
stopCluster(cl)
predictions=predict(m,test)%>%bind_cols(test)
data=predictions$.pred_class
data <- factor(data, levels = c('1', '2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'))
reference = factor(predictions$subject)
reference <- factor(reference, levels = c('1', '2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'))
confusionMatrix <- confusionMatrix(data=data, reference=reference,mode = "prec_recall")
print(i)
confusionMatrix_list <- append(confusionMatrix_list,list(confusionMatrix))
print(confusionMatrix)
rm(test,train,labeled.index,m,rf,cl,cls,array_num,predictions, data,reference, confusionMatrix)
}
