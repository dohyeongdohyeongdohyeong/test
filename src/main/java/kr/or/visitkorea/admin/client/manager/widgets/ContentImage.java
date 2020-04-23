package kr.or.visitkorea.admin.client.manager.widgets;

import static gwt.material.design.client.js.JsMaterialElement.$;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasErrorHandlers;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasValue;

import gwt.material.design.client.base.HasActivates;
import gwt.material.design.client.base.HasCaption;
import gwt.material.design.client.base.HasImage;
import gwt.material.design.client.base.HasType;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.ActivatesMixin;
import gwt.material.design.client.base.mixin.CssTypeMixin;
import gwt.material.design.client.base.mixin.ImageMixin;
import gwt.material.design.client.constants.ImageType;

//@formatter:off
//@formatter:on
public class ContentImage extends MaterialWidget implements HasCaption, HasType<ImageType>, HasImage, HasLoadHandlers, HasErrorHandlers, HasActivates, HasValue<String> {

    private CssTypeMixin<ImageType, ContentImage> typeMixin;
    private ImageMixin<ContentImage> imageMixin;
    private ActivatesMixin<ContentImage> activatesMixin;

    public ContentImage() {
        super(Document.get().createImageElement());
    }

    public ContentImage(String url) {
        this();
        setUrl(url);
    }

    public ContentImage(String url, ImageType type) {
        this(url);
        setType(type);
    }

    public ContentImage(ImageResource resource) {
        this();
        setResource(resource);
    }

    public ContentImage(ImageResource resource, ImageType type) {
        this(resource);
        setType(type);
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        $(".materialboxed").materialbox();
    }

    @Override
    public void setType(ImageType type) {
        getTypeMixin().setType(type);
    }

    @Override
    public ImageType getType() {
        return getTypeMixin().getType();
    }

    @Override
    public String getCaption() {
        return getElement().getAttribute("data-caption");
    }

    @Override
    public void setCaption(String caption) {
        getElement().setAttribute("data-caption", caption);
    }

    @Override
    public void setUrl(String url) {
        setValue(url, true);
    }

    @Override
    public String getUrl() {
        return getValue();
    }

    @Override
    public void setResource(ImageResource resource) {
        getImageMixin().setResource(resource);
    }

    @Override
    public ImageResource getResource() {
        return getImageMixin().getResource();
    }

    public int getWidth() {
        return ((ImageElement)getElement().cast()).getWidth();
    }

    public int getHeight() {
        return ((ImageElement)getElement().cast()).getHeight();
    }

    @Override
    public void setActivates(String activates) {
        getActivatesMixin().setActivates(activates);
    }

    @Override
    public String getActivates() {
        return getActivatesMixin().getActivates();
    }

    @Override
    public String getValue() {
        return getImageMixin().getUrl();
    }

    @Override
    public void setValue(String value) {
        setValue(value, false);
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        String oldValue = getUrl();
        getImageMixin().setUrl(value);

        if(fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }

    @Override
    public HandlerRegistration addLoadHandler(final LoadHandler handler) {
        return addDomHandler(handler, LoadEvent.getType());
    }

    @Override
    public HandlerRegistration addErrorHandler(final ErrorHandler handler) {
        return addDomHandler(handler, ErrorEvent.getType());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    protected CssTypeMixin<ImageType, ContentImage> getTypeMixin() {
        if (typeMixin == null) {
            typeMixin = new CssTypeMixin<>(this);
        }
        return typeMixin;
    }

    protected ImageMixin<ContentImage> getImageMixin() {
        if (imageMixin == null) {
            imageMixin = new ImageMixin<>(this);
        }
        return imageMixin;
    }

    protected ActivatesMixin<ContentImage> getActivatesMixin() {
        if (activatesMixin == null) {
            activatesMixin = new ActivatesMixin<>(this);
        }
        return activatesMixin;
    }
}

