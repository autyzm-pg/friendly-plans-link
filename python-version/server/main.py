import flask
from os import walk
app = flask.Flask(__name__)

RESOURCES=[]
for x,y,files in walk('res'):
    RESOURCES.extend(files)

print RESOURCES

@app.route('/res/<path:filepath>')
def return_resource(filepath):
    try:
        with open("res/" + filepath,"r") as fuck:
            content = fuck.read()
            content = content.encode("base64")
        return content
    except:
        flask.abort(404)

if __name__=="__main__":
    app.run()
