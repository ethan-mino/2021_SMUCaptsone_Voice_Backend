from koalanlp.Util import initialize, finalize
from koalanlp.proc import SentenceSplitter
from koalanlp import API as API
finalize()
initialize(java_options="-Xmx4g -Dfile.encoding=utf-8", OKT="2.0.2")
