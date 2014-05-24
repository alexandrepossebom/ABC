package com.possebom.alfabeto;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class ShakeSprite extends Sprite {
	private boolean mShaking;
	private float mDuration;
	private float mIntensity;
	private float mCurrentDuration;
	private String letter;
	private boolean hide = true;

	public String getLetter() {
		return letter;
	}

	private float mX2, mY2;
	private Main main;

	public ShakeSprite(final int i, final int j, final TextureRegion mt, final String string) {
		super(i, j, mt);
		super.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.letter = string;
	}

	public ShakeSprite(final int i, final int j, final TextureRegion mt, final String string, final Main main) {
		super(i, j, mt);
		super.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.letter = string;
		this.main = main;
	}

	public boolean isHide() {
		return hide;
	}

	public void hide(final boolean hide) {
		this.hide = hide;
	}

	public void shake(final float pDuration, final float pIntensity) {
		mX2 = this.getX();
		mY2 = this.getY();
		mShaking = true;
		mDuration = pDuration;
		mIntensity = pIntensity;
		mCurrentDuration = 0;
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if (!pSceneTouchEvent.isActionDown() || isHide())
			return true;
		main.letterTouched(this);
		return true;
	}

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (mShaking) {
			mCurrentDuration += pSecondsElapsed;
			if (mCurrentDuration > mDuration) {
				mShaking = false;
				mCurrentDuration = 0;
				this.setPosition(mX2, mY2);
			} else {
				int sentitX = 1;
				if (Math.random() < 0.5)
					sentitX = -1;
				this.setPosition((float) (getX() + Math.random() * mIntensity * sentitX), (float) (getY()));
			}
		}
	}
}
