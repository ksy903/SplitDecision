package com.JaeLee.splitdecision;

import com.badlogic.gdx.Game;
import com.JaeLee.splitdecision.screens.Logo;

public class SplitDecision extends Game {
	@Override
	public void create () {
		setScreen(new Logo());
	}
}
