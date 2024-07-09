package com.careerhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Human_Resources")
public class HumanResources extends User{
    private List<User> allUserApplied = new ArrayList<>(); // all users that apply to one single company
}
