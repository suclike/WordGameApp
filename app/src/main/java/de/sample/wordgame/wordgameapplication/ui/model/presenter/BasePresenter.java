package de.sample.wordgame.wordgameapplication.ui.model.presenter;

class BasePresenter<V> implements Presenter<V> {

    protected V view;

    @Override
    public void init(final V view) {
        this.view = view;
        checkViewAttached();
    }

    @Override
    public void destroy() {
        view = null;
    }

    private void checkViewAttached() {
        if (!isViewAttached()) {
            throw new ViewNotAttachedException();
        }
    }

    V getAttachedView() {
        return view;
    }

    private boolean isViewAttached() {
        return view != null;
    }

    private static class ViewNotAttachedException extends RuntimeException {
        private ViewNotAttachedException() {
            super("Please call Presenter.init(view) before" + " requesting data from the Presenter");
        }
    }
}
