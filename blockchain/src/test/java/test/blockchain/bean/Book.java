package test.blockchain.bean;


public class Book {
 private String name;
 private String author;
 
 
 public Book(){
  
 } 
 public void setAuthor(String author) {
  this.author = author; 
 }

 public String getAuthor() {
  return (this.author); 
 }
 public void setName(String name){
  this.name=name;
 } 
 public String getName(){
  return this.name;
 }
}