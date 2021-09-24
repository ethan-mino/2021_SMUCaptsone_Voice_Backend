from django import forms

STYLE_KEY_YO = "yo"
STYLE_KEY_SHO = "sho"
STYLE_KEY_BAN = "ban"

MY_CHOICES = (
    (STYLE_KEY_SHO, '합쇼체'),
    (STYLE_KEY_BAN, '반말체'),
)

class StyleTransferForm(forms.Form):
  input_text = forms.CharField(widget=forms.Textarea(attrs={'rows': 5, 'cols': 80}))
  enable_backtrans = forms.BooleanField(widget=forms.CheckboxInput(), required=False)
  target_style = forms.ChoiceField(choices=MY_CHOICES, widget=forms.Select(attrs={'class': 'form-control', 'style': 'width: 68%;'}))
