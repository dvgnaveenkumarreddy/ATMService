package com.atm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Denomination {

	private int fives;

	private int tens;

	private int twenties;

	private int fifties;

}
