package edu.tcu.cs.hogwarts_artifacts_online.client.ai.chat.dto;

//role can be System,user,assistant
//content can be Instructions,Questions,Examples,Statements
public record Message(String role,String content) {

}
