package com.example.ds1.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "text_word")
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "pos")
    Long pos;

    @Column(name = "text")
    String text;

    @Column(name = "word")
    String word;

}
