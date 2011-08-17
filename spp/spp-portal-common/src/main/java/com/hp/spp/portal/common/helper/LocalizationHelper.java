package com.hp.spp.portal.common.helper;

import org.apache.log4j.Logger;

import com.epicentric.template.Style;
import com.epicentric.template.Template;
import com.epicentric.template.TemplateManager;
import com.hp.spp.cache.Cache;
import com.hp.spp.profile.Constants;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.vignette.portal.website.enduser.PortalContext;

public class LocalizationHelper {

	private static Logger mLog = Logger.getLogger(LocalizationHelper.class);

	/**
	 * Gets the ID of the Style that contains the site translations. The ID is
	 * kept in cache. On the first access to this method the
	 * sppLocalization_type is sought and from there the site-specific style is
	 * retrieved. Once the style is retrieved, the id is put in the cache and
	 * returned.
	 * 
	 * @param context
	 * @return
	 */
	public String getCommonI18nID(PortalContext context) {

		String siteName = new ProfileHelper().getProfileValue(context,
				Constants.MAP_SITE);
		GeneralCacheAdministrator cache = Cache.getInstance();
		String localizationKey = "localizationId-" + siteName;
		String i18nID = null;
		try {
			i18nID = (String) cache.getFromCache(localizationKey);
		} catch (NeedsRefreshException e) {
			boolean updated = false;
			try {
				if (mLog.isDebugEnabled()) {
					mLog.debug("i18n ID not found in cache for key "
							+ localizationKey);
				}
				Template styleType = TemplateManager.getInstance()
						.getTemplateByFriendlyID("sppLocalization_type");
				if (styleType != null) {
					Style style = styleType.getStyleByFriendlyID(siteName
							+ "localization");
					if (style != null) {
						i18nID = style.getUID();
						cache.putInCache(localizationKey, i18nID);
						updated = true;
						if (mLog.isDebugEnabled()) {
							mLog.debug("Putting i18n ID in cache: key="
									+ localizationKey + ", value=" + i18nID);
						}
					} else {
						mLog.error("Style does not exist: " + siteName
								+ "localization");
					}
				} else {
					mLog.error("Style type does not exist: sppLocalization_type");
				}
			} finally {
				// cancel the update if we do not update the cache
				if (!updated) {
					cache.cancelUpdate(localizationKey);
				}
			}
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("returning localization id: " + i18nID);
		}

		return i18nID;
	}

	public String getValueNoSpan(String initValue) {
		if (initValue == null) {
			return "";
		}

		String tmpValue = initValue.toLowerCase();
		if (!tmpValue.equals("") && tmpValue.indexOf("<span") != -1
				&& tmpValue.indexOf("</span>") != -1) {
			int pos1 = tmpValue.indexOf(">");
			int pos2 = tmpValue.lastIndexOf("<");
			if (pos1 < pos2) {
				initValue = initValue.substring(pos1 + 1, pos2);
			} else {
				return "";
			}
		}

		return initValue;
	}

}
