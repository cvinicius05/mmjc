/* This file was generated by SableCC (http://www.sablecc.org/). */

package ufc.ck017.mmjc.node;

import ufc.ck017.mmjc.analysis.*;

@SuppressWarnings("nls")
public final class AGthanExpression extends PExpression
{
    private PExpression _l_;
    private PExpression _r_;

    public AGthanExpression()
    {
        // Constructor
    }

    public AGthanExpression(
        @SuppressWarnings("hiding") PExpression _l_,
        @SuppressWarnings("hiding") PExpression _r_)
    {
        // Constructor
        setL(_l_);

        setR(_r_);

    }

    @Override
    public Object clone()
    {
        return new AGthanExpression(
            cloneNode(this._l_),
            cloneNode(this._r_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAGthanExpression(this);
    }

    public PExpression getL()
    {
        return this._l_;
    }

    public void setL(PExpression node)
    {
        if(this._l_ != null)
        {
            this._l_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._l_ = node;
    }

    public PExpression getR()
    {
        return this._r_;
    }

    public void setR(PExpression node)
    {
        if(this._r_ != null)
        {
            this._r_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._r_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._l_)
            + toString(this._r_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._l_ == child)
        {
            this._l_ = null;
            return;
        }

        if(this._r_ == child)
        {
            this._r_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._l_ == oldChild)
        {
            setL((PExpression) newChild);
            return;
        }

        if(this._r_ == oldChild)
        {
            setR((PExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }

	@Override
	public int getLine() {
		return _r_.getLine();
	}

	@Override
	public int getPos() {
		return _r_.getPos();
	}
}
