package com.possebom.alfabeto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.FadeOutModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class Main extends BaseGameActivity implements OnInitListener {
	private final static String[] ALFABETO = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Z" };

	private final static int CAMERA_WIDTH = 800;
	private final static int CAMERA_HEIGHT = 480;

	private TextToSpeech mTts;

	private String sorted = "";
	private final ArrayList<String> letras = new ArrayList<String>();
	private final Random generator = new Random();
	private boolean hasTts = false;
	private int erros = 0;

	private Camera mCamera;
	private ShakeSprite mShakeSprite[] = new ShakeSprite[ALFABETO.length];

	private BitmapTextureAtlas mbPlay;
	private TextureRegion mtPlay;

	private BitmapTextureAtlas mbRepeat;
	private TextureRegion mtRepeat;

	private ChangeableText elapsedText;

	private TextureRegion[] mTextureRegion = new TextureRegion[ALFABETO.length];
	private Sound[] mSound = new Sound[ALFABETO.length];

	private Sound beep;
	private Sound yeah;

	private BitmapTextureAtlas mFontTexture;
	private Font mFont;

	@Override
	public Engine onLoadEngine() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera).setNeedsSound(true));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mFontTexture = new BitmapTextureAtlas(256, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, Color.WHITE);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);

		mbPlay = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mtPlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mbPlay, this, "play.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mbPlay);

		mbRepeat = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mtRepeat = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mbRepeat, this, "mic.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mbRepeat);

		SoundFactory.setAssetBasePath("sfx/");
		for (int i = 0; i < ALFABETO.length; i++) {
			final String pngFile = ALFABETO[i].toLowerCase(Locale.getDefault()) + ".png";
			final String wavFile = ALFABETO[i].toLowerCase(Locale.getDefault()) + ".wav";

			final BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

			mTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, pngFile, 0, 0);
			try {
				mSound[i] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, wavFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			beep = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "beep.wav");
			yeah = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "yeah.wav");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Scene onLoadScene() {
		final Scene scene = new Scene();
		scene.setBackground(new ColorBackground(0, 0.2f, 0));
		elapsedText = new ChangeableText(500, 14, mFont, "Erros: 0", "Erros: XXXX".length());
		scene.attachChild(elapsedText);
		return scene;
	}

	@Override
	public void onLoadComplete() {
		createSprite();
		mTts = new TextToSpeech(getApplicationContext(), this);
		letras.clear();
		letras.addAll(Arrays.asList(ALFABETO));
	}

	public void sort() {
		if (letras.size() == 0) {
			sorted = "";
			yeah.play();
		} else {
			final int rand = generator.nextInt(letras.size());
			sorted = letras.get(rand);
		}
	}

	private void createSprite() {
		final Scene scene = mEngine.getScene();

		final Sprite spritePlay = new Sprite(CAMERA_WIDTH / 2 - 128, 10, mtPlay) {
			@Override
			public boolean onAreaTouched(TouchEvent mTouchEvent, float mTouchAreaLocalX, float mTouchAreaLocalY) {
				if (mTouchEvent.isActionDown()) {
					for (ShakeSprite sprite : mShakeSprite) {
						sprite.setAlpha(1.0f);
						sprite.hide(false);
					}
					letras.clear();
					letras.addAll(Arrays.asList(ALFABETO));
					erros = 0;
					updateErros();
					sort();
					playSound(sorted);
				}
				return true;
			}
		};
		scene.registerTouchArea(spritePlay);
		scene.attachChild(spritePlay);

		final Sprite spriteRepeat = new Sprite(CAMERA_WIDTH / 2, 10, mtRepeat) {
			@Override
			public boolean onAreaTouched(final TouchEvent mTouchEvent, final float mTouchAreaLocalX, final float mTouchAreaLocalY) {
				if (mTouchEvent.isActionDown()) {
					repeat();
				}
				return true;
			}
		};
		scene.registerTouchArea(spriteRepeat);
		scene.attachChild(spriteRepeat);

		final int lineSize = CAMERA_HEIGHT / 6;
		final int colSize = CAMERA_WIDTH / 5;
		final int borderSize = 40;

		int col = 0;
		int line = 0;
		for (int i = 0; i < ALFABETO.length; i++) {
			if (i % 5 == 0) {
				line++;
				col = 0;
			}
			mShakeSprite[i] = new ShakeSprite(colSize * col++ + borderSize, lineSize * line, mTextureRegion[i], ALFABETO[i], this);
			scene.attachChild(mShakeSprite[i]);
			scene.registerTouchArea(mShakeSprite[i]);
		}

	}

	public boolean letterTouched(final ShakeSprite shakeSprite) {
		final FadeOutModifier fadeOut = new FadeOutModifier(1.0f, EaseLinear.getInstance());
		if (sorted.equals(shakeSprite.getLetter())) {
			shakeSprite.registerEntityModifier(fadeOut);
			shakeSprite.hide(true);
			letras.remove(shakeSprite.getLetter());
			sort();
			playSound(sorted);
		} else {
			shakeSprite.shake(0.5f, 2.0f);
			beep.play();
			erros++;
			updateErros();
		}
		return true;
	}

	private void updateErros() {
		elapsedText.setText("Erros: " + erros);
	}

	private void repeat() {
		if (hasTts) {
			mTts.speak(Utils.letterToWord(sorted), TextToSpeech.QUEUE_FLUSH, null);
		} else {
			playSound(sorted);
		}
	}

	private void playSound(final String str) {
		if (hasTts) {
			mTts.speak(Utils.fixLetter(str), TextToSpeech.QUEUE_FLUSH, null);
		} else {
			final int index = Arrays.asList(ALFABETO).indexOf(str);
			if (index >= 0 && index < ALFABETO.length) {
				mSound[index].play();
			}
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			try {
				if (mTts.isLanguageAvailable(new Locale("pt", "BR")) == 1) {
					hasTts = true;
				}
			} catch (Exception e) {
				hasTts = false;
			}
		}
	}
}