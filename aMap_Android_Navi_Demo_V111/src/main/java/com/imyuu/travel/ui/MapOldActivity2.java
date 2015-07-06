package com.imyuu.travel.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imyuu.travel.R;
import com.imyuu.travel.bean.IuuTitleModel;
import com.imyuu.travel.bean.MapIuuObject;
import com.imyuu.travel.bean.MapLineObject;
import com.imyuu.travel.bean.MapVoiceObject;
import com.imyuu.travel.bean.RecommendLinesectionModel;
import com.imyuu.travel.bean.RecommendLinesectionguideModel;
import com.imyuu.travel.bean.ScenicMapOldModel;
import com.imyuu.travel.bean.ScenicOldModel;
import com.imyuu.travel.bean.ScenicRecommendLineModel;
import com.imyuu.travel.database.RecommendLinesectionDataHelper;
import com.imyuu.travel.database.RecommendLinesectionguideDataHelper;
import com.imyuu.travel.database.ScenicDataHelper;
import com.imyuu.travel.database.ScenicMapDataHelper;
import com.imyuu.travel.database.ScenicRecommendLineDataHelper;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.util.ConstantsOld;
import com.imyuu.travel.util.MediaHelper;
import com.imyuu.travel.view.IuuMapWidget;
import com.inttus.app.annotation.Gum;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapLayer;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.ls.widgets.map.utils.PivotFactory.PivotPosition;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class MapOldActivity2 extends TravelActvity implements OnClickListener {
	@Gum(resId = R.id.travel_map)
	RelativeLayout mapLayout;
	@Gum(resId = R.id.button_map_menu_choice)
	public Button buttonMapMenuChoice;
	@Gum(resId = R.id.linearlayout_map_line)
	public LinearLayout linearlayoutMapLine;
	@Gum(resId = R.id.horizontalscrollview_map_line)
	private HorizontalScrollView horizontalscrollviewMapLine;
	private IuuMapWidget map;
	private IuuTitleModel title;
	private String scenicId;
	private ScenicAreaJson scenicModel;
	String lineId;
	private Point pointCur;
	private HashMap<String, ScenicMapOldModel> scenicMapHashMap;
	private List<ScenicMapOldModel> scenicMapModelList;
	private List<ScenicMapOldModel> scenicMapModelHasVoiceList;
	private List<Point> points;
	private List<Point> pointsScenic;
	private boolean isSelectLine = false;
	private MapVoiceObject currentVoiceObject;
	private Layer iuuLayer;
	private MapIuuObject iuuObject;
	private MapWidgetHelper helper = new MapWidgetHelper();

	private static class MapWidgetHelper implements OnMapTouchListener {
		MapOldActivity2 self;

		@Override
		public void onTouch(MapWidget v, MapTouchedEvent event) {
			ArrayList<ObjectTouchEvent> events = event.getTouchedObjectEvents();
			if (events != null && events.size() > 0) {
				ObjectTouchEvent e = events.get(0);
				Layer layer = self.map.getLayerById(e.getLayerId());
				int id = (int) e.getLayerId();
				MapObject object = layer.getMapObject(0);
				if (object == null || !(object instanceof MapVoiceObject)) {
					return;
				}
				MapVoiceObject object2 = (MapVoiceObject) object;
				
				if (self.currentVoiceObject == object2) {
					object2.setState(CONS_APP_MEDIA_READY);
					return;
				}
				
				self.map.scrollMapTo(self.pointsScenic.get(id));
				self.resetMedia(object2);
				object2.setMediaUrl(ConstantsOld.SCENIC_SINGLE_FILE_PATH
						+ self.scenicModel.getScenicId()
						+ "/"
						+ self.scenicMapModelHasVoiceList.get(id)
								.getScenicspotVoice());
				if (object2.getState() == CONS_APP_MEDIA_READY) {
					object2.setState(CONS_APP_MEDIA_PLAYING);
				} else if (object2.getState() == CONS_APP_MEDIA_PLAYING) {
					object2.setState(CONS_APP_MEDIA_RESUME);
				} else if (object2.getState() == CONS_APP_MEDIA_RESUME) {
					object2.setState(CONS_APP_MEDIA_RESTART);
				} else {
					object2.setState(CONS_APP_MEDIA_READY);
				}
			}
		}

	}
	
	private void resetMedia(MapVoiceObject voiceObject){
		MediaHelper.release();
		Layer layer = null;
		MapObject object = null;
		for (int i = 0; i < map.getLayerCount(); i++) {
			layer = map.getLayer(i);
			object = layer.getMapObject(0);
			if (object == null || !(object instanceof MapVoiceObject)) {
				continue;
			}
			MapVoiceObject object2 = (MapVoiceObject) object;
			object2.setState(CONS_APP_MEDIA_READY);
		}
		currentVoiceObject = voiceObject;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		title = new IuuTitleModel(this, R.id.image_search_back,
				R.id.text_app_title);
		bindViews();
		buttonMapMenuChoice.setOnClickListener(this);
		initData();
		initMap(savedInstanceState);
	}

	private void initMap(Bundle savedInstanceState) {
		map = new IuuMapWidget(savedInstanceState, this, new File(getPathFor(
				scenicId, 3)), 12);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		map.setMinZoomLevel(CONS_APP_IUU_MAP_MIN_LEVEL);
		map.setMaxZoomLevel(CONS_APP_IUU_MAP_MAX_LEVEL);
		map.setOnMapTouchListener(helper);
		helper.self = this;
		// map.add
		OfflineMapConfig config = map.getConfig();
		config.setPinchZoomEnabled(true); // Sets pinch gesture to zoom
		config.setFlingEnabled(true); // Sets inertial scrolling of the map
		config.setMaxZoomLevelLimit(20);
		config.setZoomBtnsVisible(false); // Sets embedded zoom buttons visible

		MapGraphicsConfig graphicsConfig = config.getGraphicsConfig();
		graphicsConfig.setAccuracyAreaColor(0x002FB3F1);
		graphicsConfig.setAccuracyAreaBorderColor(0x002FB3F1); // Blue without
		// transparency
		mapLayout.addView(map, params);
		mapLayout.setBackgroundColor(0x002FB3F1);
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent != null) {
//			scenicId = intent.getStringExtra(ConstantsOld.SCIENCE_ID_KEY);
		scenicId = "221";
		}

		if (TextUtils.isEmpty(scenicId)) {
			showShort(R.string.index_loading_fail);
			finish();
		} else {

			MediaHelper.s().setOnCompletionListener(
					new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							MediaHelper.release();
						}
					});
			getThread(0).start();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == buttonMapMenuChoice) {
			getThread(1).start();
		}
	}

	private void loadMap(String lineId, boolean isSelectLine) {
		this.lineId = lineId;
		// 初始化推荐游览线路时
		if (!TextUtils.isEmpty(lineId)) {
			ScenicMapDataHelper scenicMapDataHelper = new ScenicMapDataHelper(
					MapOldActivity2.this);
			RecommendLinesectionDataHelper recommendLinesectionDataHelper = new RecommendLinesectionDataHelper(
					MapOldActivity2.this);
			RecommendLinesectionguideDataHelper recommendLinesectionguideDataHelper = new RecommendLinesectionguideDataHelper(
					MapOldActivity2.this);
			scenicMapModelList = scenicMapDataHelper
					.getListByScenicId(scenicId);
			pointsScenic = new ArrayList<Point>();
			scenicMapModelHasVoiceList = new ArrayList<ScenicMapOldModel>();
			// 获取所有带有解说音频的景点
			for (ScenicMapOldModel scenicMapModel : scenicMapModelList) {
				if (!TextUtils.isEmpty(scenicMapModel.getScenicspotVoice())) {
					pointsScenic.add(changeXY(
							scenicMapModel.getRelativeLongitude(),
							scenicMapModel.getRelativeLatitude(),
							scenicModel.getScenicmapMaxy()));
					scenicMapModelHasVoiceList.add(scenicMapModel);
				}
			}
			List<RecommendLinesectionModel> recommendLinesectionModels = recommendLinesectionDataHelper
					.getListByRouteId(lineId);
			points = new ArrayList<Point>();
			if (recommendLinesectionModels.size() > 0) {
				int index = 0;
				scenicMapHashMap = new HashMap<String, ScenicMapOldModel>();
				linearlayoutMapLine.removeAllViews();
				for (RecommendLinesectionModel recommendLinesectionModel : recommendLinesectionModels) {
					ScenicMapOldModel scenicMapModelA = scenicMapDataHelper
							.getModelById(recommendLinesectionModel
									.getAspotId());
					ScenicMapOldModel scenicMapModelB = scenicMapDataHelper
							.getModelById(recommendLinesectionModel
									.getBspotId());
					scenicMapHashMap.put(scenicMapModelA.getScenicMapId(),
							scenicMapModelA);
					scenicMapHashMap.put(scenicMapModelB.getScenicMapId(),
							scenicMapModelB);
					// 动态添加景点布局
					LinearLayout linearLayoutMapLineItem = (LinearLayout) getView(R.layout.map_line_item);
					linearLayoutMapLineItem.setTag(scenicMapModelA
							.getScenicMapId());
					TextView textMapLineTitle = (TextView) linearLayoutMapLineItem
							.findViewById(R.id.text_map_line_title);
					textMapLineTitle.setText(scenicMapModelA
							.getScenicspotName());
					ImageView imageMapLinePoint = (ImageView) linearLayoutMapLineItem
							.findViewById(R.id.image_map_line_point);
					// index等于0代表为线路上的第一个点
					if (index == 0) {
						imageMapLinePoint
								.setImageResource(R.drawable.img_map_point_checked);
						imageMapLinePoint
								.setBackgroundResource(R.drawable.img_map_line_r_half);
						pointCur = changeXY(
								scenicMapModelA.getRelativeLongitude(),
								scenicMapModelA.getRelativeLatitude(),
								scenicModel.getScenicmapMaxy());
					} else {
						imageMapLinePoint
								.setImageResource(R.drawable.img_map_point);
					}
					linearLayoutMapLineItem
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									for (int i = 0; i < linearlayoutMapLine
											.getChildCount(); i++) {
										View view = linearlayoutMapLine
												.getChildAt(i);
										if (view.getTag().toString()
												.equals(v.getTag().toString())) {
											ScenicMapOldModel scenicMapModel = scenicMapHashMap
													.get(v.getTag().toString());
											if (scenicMapModel != null) {
												ImageView imageMapLinePoint = (ImageView) v
														.findViewById(R.id.image_map_line_point);
												imageMapLinePoint
														.setImageResource(R.drawable.img_map_point_checked);
												Point pointFrom = pointCur;
												pointCur = changeXY(
														scenicMapModel
																.getRelativeLongitude(),
														scenicMapModel
																.getRelativeLatitude(),
														scenicModel
																.getScenicmapMaxy());

												moveIuuFrom(pointFrom);
											}
										} else {
											ImageView imageMapLinePoint = (ImageView) view
													.findViewById(R.id.image_map_line_point);
											imageMapLinePoint
													.setImageResource(R.drawable.img_map_point);
										}
									}
								}
							});
					LinearLayout.LayoutParams lp;
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					// 判断手机分辨率，设定景点图标之间的距离
					if (dm.widthPixels <= 480 && dm.heightPixels <= 800) {
						lp = new LinearLayout.LayoutParams(130,
								LinearLayout.LayoutParams.MATCH_PARENT);
					} else {
						lp = new LinearLayout.LayoutParams(230,
								LinearLayout.LayoutParams.MATCH_PARENT);
					}
					lp.weight = 1;
					linearLayoutMapLineItem.setLayoutParams(lp);
					linearlayoutMapLine.addView(linearLayoutMapLineItem);
					index++;
					// 当index等于路线list大小的时候，表示游标到了最后一个点
					if (index == recommendLinesectionModels.size()) {
						linearLayoutMapLineItem = (LinearLayout) getView(R.layout.map_line_item);
						linearLayoutMapLineItem.setTag(scenicMapModelB
								.getScenicMapId());
						textMapLineTitle = (TextView) linearLayoutMapLineItem
								.findViewById(R.id.text_map_line_title);
						textMapLineTitle.setText(scenicMapModelB
								.getScenicspotName());
						imageMapLinePoint = (ImageView) linearLayoutMapLineItem
								.findViewById(R.id.image_map_line_point);
						imageMapLinePoint
								.setImageResource(R.drawable.img_map_point);
						imageMapLinePoint
								.setBackgroundResource(R.drawable.img_map_line_l_half);
						linearLayoutMapLineItem
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										for (int i = 0; i < linearlayoutMapLine
												.getChildCount(); i++) {
											View view = linearlayoutMapLine
													.getChildAt(i);
											if (view.getTag()
													.toString()
													.equals(v.getTag()
															.toString())) {
												ScenicMapOldModel scenicMapModel = scenicMapHashMap
														.get(v.getTag()
																.toString());
												if (scenicMapModel != null) {
													ImageView imageMapLinePoint = (ImageView) v
															.findViewById(R.id.image_map_line_point);
													imageMapLinePoint
															.setImageResource(R.drawable.img_map_point_checked);
													Point pointFrom = pointCur;
													pointCur = changeXY(
															scenicMapModel
																	.getRelativeLongitude(),
															scenicMapModel
																	.getRelativeLatitude(),
															scenicModel
																	.getScenicmapMaxy());
													moveIuuFrom(pointFrom);
												}
											} else {
												ImageView imageMapLinePoint = (ImageView) view
														.findViewById(R.id.image_map_line_point);
												imageMapLinePoint
														.setImageResource(R.drawable.img_map_point);
											}
										}
									}
								});
						if (dm.widthPixels <= 480 && dm.heightPixels <= 800) {
							lp = new LinearLayout.LayoutParams(130,
									LinearLayout.LayoutParams.MATCH_PARENT);
						} else {
							lp = new LinearLayout.LayoutParams(230,
									LinearLayout.LayoutParams.MATCH_PARENT);
						}
						lp.weight = 1;
						linearLayoutMapLineItem.setLayoutParams(lp);
						linearlayoutMapLine.addView(linearLayoutMapLineItem);
					}
					// 将路线中的所有坐标点按照顺序添加到list中
					List<RecommendLinesectionguideModel> recommendLinesectionguideModelList = recommendLinesectionguideDataHelper
							.getListByDetailId(recommendLinesectionModel
									.getRecommendLinesectionId());
					points.add(changeXY(scenicMapModelA.getRelativeLongitude(),
							scenicMapModelA.getRelativeLatitude(),
							scenicModel.getScenicmapMaxy()));
					for (RecommendLinesectionguideModel recommendLinesectionguideModel : recommendLinesectionguideModelList) {
						points.add(changeXY(recommendLinesectionguideModel
								.getRelativeLongitude(),
								recommendLinesectionguideModel
										.getRelativeLatitude(), scenicModel
										.getScenicmapMaxy()));
					}
					points.add(changeXY(scenicMapModelB.getRelativeLongitude(),
							scenicMapModelB.getRelativeLatitude(),
							scenicModel.getScenicmapMaxy()));

				}
			}
			scenicMapDataHelper.close();
			recommendLinesectionDataHelper.close();
			recommendLinesectionguideDataHelper.close();
			map.setPoints(points);
			initScenicPoint();
		}
		// 判断是否选过路线
		if (isSelectLine) {
			horizontalscrollviewMapLine.setVisibility(View.VISIBLE);
			if (points != null && points.size() > 0) {
				pointCur = points.get(0);
				if (iuuObject == null) {
					initIuuPoint(false);
				}
				iuuObject.moveTo(pointCur.x, pointCur.y);
				map.scrollMapTo(pointCur);
			}
		} else {
			// 第一次选择路线并加载地图
			horizontalscrollviewMapLine.setVisibility(View.GONE);
		}
		initIuuPoint(false);
	}

	Map<Integer, Handler> handlerMap = new HashMap<Integer, Handler>();

	private Handler getHandler(final int type) {
		Handler handler = handlerMap.get(type);
		if (handler == null) {
			handler = new Handler() {

				@SuppressWarnings("unchecked")
				@Override
				public void handleMessage(Message msg) {
					title.progress(false);
					int code = msg.arg1;
					if (type == 0) {
						if (code == -1) {
							showShort(R.string.index_loading_fail);
							finish();
						} else {
							scenicModel = (ScenicAreaJson) msg.obj;
							title.setTitle(scenicModel.getScenicName());
						}
					} else if (type == 1) {
						if (code == 0) {
							final ArrayList<ScenicRecommendLineModel> scenicRecommendLineModelArrayList = (ArrayList<ScenicRecommendLineModel>) msg.obj;

							String[] items = new String[scenicRecommendLineModelArrayList
									.size()];
							for (int i = 0; i < scenicRecommendLineModelArrayList
									.size(); i++) {
								items[i] = scenicRecommendLineModelArrayList
										.get(i).getRecommendRoutename();
							}
							choiceMenu(items,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											isSelectLine = true;
											ScenicRecommendLineModel scenicRecommendLineModel = scenicRecommendLineModelArrayList
													.get(which);
											loadMap(scenicRecommendLineModel
													.getScenicRecommendLineId(),
													isSelectLine);
										}
									});
						}
					}
				}

			};
			handlerMap.put(type, handler);
		}
		return handler;
	}

	private Thread getThread(final int type) {
		getHandler(type);
		title.progress(true);
		return new Thread() {

			@Override
			public void run() {
				Message message = new Message();
				int code = -1;
				Object result = null;
				if (type == 0) {
					result = ScenicAreaJson.load(scenicId);
					code = 0;
				} else if (type == 1) {
					ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(
							MapOldActivity2.this);
					try {
						ArrayList<ScenicRecommendLineModel> scenicRecommendLineModelArrayList = scenicRecommendLineDataHelper
								.getListByScenicId(scenicModel.getScenicId());
						// 加载路线名称
						result = scenicRecommendLineModelArrayList;
						code = 0;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							scenicRecommendLineDataHelper.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
				message.arg1 = code;
				message.obj = result;
				getHandler(type).sendMessage(message);
			}

		};
	}

	// 坐标系转化方法
	private Point changeXY(double x, double y, int maxy) {
		int rx = (int) x;
		int ry = (int) (maxy - y);
		return new Point(rx, ry);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		MediaHelper.release();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MediaHelper.release();
		super.onDestroy();
	}
	
	private void initScenicPoint(){
		Layer layer = null;
		Drawable drawable = getResources().getDrawable(
				R.drawable.img_map_voice);
		map.removeAllLayers();
		MapVoiceObject mapObject = null;
		Point point = null;
		Point point2 = PivotFactory.createPivotPoint(drawable,
				PivotPosition.PIVOT_BOTTOM_CENTER);
		if (pointsScenic != null && pointsScenic.size() > 0) {
			for (int i = 0; i < pointsScenic.size(); i++) {
				layer = map.createLayer(i);
				point = pointsScenic.get(i);
				mapObject = new MapVoiceObject(0, drawable,
						point, point2, true, true);
				mapObject.setContext(MapOldActivity2.this);
				layer.addMapObject(mapObject);
			}
			
			resetMedia(null);
		}
		initIuuLine();
	}
	
	private void initIuuLine(){
		int layers = map.getLayerCount();
		for (int i = 0; i < layers; i++) {
			MapLayer layer = (MapLayer) map.getLayer(i);
			MapObject object = layer.getMapObject(0);
			if (object != null && (object instanceof MapLineObject)) {
				map.removeLayer(layer.getId());
			}
		}
		
		if (map.getPoints() != null && map.getPoints().size() > 0) {
			Layer layer = null;
			Drawable drawable = getResources().getDrawable(
					R.drawable.gray_point);
			Point point2 = PivotFactory.createPivotPoint(drawable,
					PivotPosition.PIVOT_CENTER);
			MapLineObject object = null;
			int id = 20000;
			for (Point p: map.getPoints()) {
				layer = map.createLayer(id);
				object = new MapLineObject(0, drawable, p, point2, false, true);
				object.setDrawable(getResources().getDrawable(R.drawable.gray_point));
				layer.addMapObject(object);
				id += 1;
			}
		}
		initIuuPoint(false);
	}
	
	private void initIuuPoint(boolean move){
		if (pointCur == null) {
			return;
		}
		if (iuuLayer != null) {
			map.removeLayer(CONS_APP_IUU_LAYER_ID);
		}
		Drawable drawable = getResources().getDrawable(
				R.drawable.icon_map_point);
		Point point2 = PivotFactory.createPivotPoint(drawable,
				PivotPosition.PIVOT_BOTTOM_CENTER);
		iuuLayer = map.createLayer(CONS_APP_IUU_LAYER_ID);
		iuuObject = new MapIuuObject(0, drawable, pointCur, point2, true, true);
		iuuLayer.addMapObject(iuuObject);
	}
	
	private void moveIuuFrom(final Point from){
		if (iuuLayer == null || iuuObject == null) {
			initIuuPoint(false);
		}
		
		final Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				int w = msg.what;
				if (points.size() > w) {
					Point point = points.get(w);
					iuuObject.moveTo(point.x, point.y);
					map.scrollMapTo(point);
				}
			}
			
		};
		new Thread(){

			@Override
			public void run() {
				int f = points.indexOf(from);
				int t = points.indexOf(pointCur);
				double oDis = getDex(from, pointCur), nDis = 0.0, dix = 1.0;
				Point fp = null, tp = null;
				int sp = CONS_APP_IUU_MOVE_SPEED;
				double spd = sp;
				if (f <= t) {
					for (int i = f; i <= t; i++) {
						try {
							Thread.sleep(sp);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message message = new Message();
						message.what = i;
						handler.sendMessage(message);
						dix = 1.0;
						if (i < points.size() - 1) {
							fp = points.get(i);
							tp = points.get(i + 1);
							nDis = getDex(fp, tp);
							dix = oDis / nDis;
							spd = ((double)CONS_APP_IUU_MOVE_SPEED) * dix;
							sp = (int)spd;
						}
					}
				} else {
					for (int i = f; i >= t; i--) {
						try {
							Thread.sleep(sp);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Message message = new Message();
						message.what = i;
						handler.sendMessage(message);
						dix = 1.0;
						if (i > 0) {
							fp = points.get(i);
							tp = points.get(i - 1);
							nDis = getDex(fp, tp);
							dix = oDis / nDis;
							spd = ((double)CONS_APP_IUU_MOVE_SPEED) * dix;
							sp = (int)spd;
						}
					}
				}
			}
			
		}.start();
	}
	
	private double getDex(Point f, Point t){
		if (f != null && t != null) {
			return Math.pow(Math.pow(f.x - t.x, 2) + Math.pow(f.y - t.y, 2), 1 / 2);
		}
		return 0.0;
	}
}
