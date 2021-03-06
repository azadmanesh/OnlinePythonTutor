# Lightweight OPT server that works on both Python 2 and 3

# to invoke, run 'python bottle_server.py'
# and visit http://localhost:8080/index.html
#
# external dependencies: bottle
#
# easy_install pip
# pip install bottle

# From an OPT user: A couple notes: to get bottle_server.py running,
# I had to replace cStringIO with io and urllib2 with urllib, for
# compatibility from 2.x to 3.x Ii was running from /v3/).

from bottle import route, get, request, run, template, static_file
from subprocess import call, check_call, CalledProcessError
import StringIO # NB: don't use cStringIO since it doesn't support unicode!!!
import json
import pg_logger
import urllib
import urllib2
import sys
import os
import shutil


# dummy routes for testing only
@route('/web_exec_<name:re:.+>.py')
def web_exec(name):
    print >> sys.stderr, '1'
    return 'OK'

@route('/LIVE_exec_<name:re:.+>.py')
def live_exec(name):
    print >> sys.stderr, '2'
    return 'OK'


@route('/<filepath:path>')
def index(filepath):
    print >> sys.stderr, '3'
    # special-case for testing name_lookup.py ...
    if 'name_lookup.py' in filepath:
        return json.dumps(dict(name='TEST NAME', email='TEST EMAIL'))
    return static_file(filepath, root='.')



@get('/exec')
def get_exec():
  print >> sys.stderr, 'ID:' 
  print >> sys.stderr, get_exec.id
  get_exec.id+=1
  print >> sys.stderr, 'Hi from exec'
  print >> sys.stderr, request.query.user_script

  source = request.query.user_script
  javaTutorPlusWorkingDir = '/Users/hauswirm/tools/OnlinePythonTutor/wd/'
  currentTargetPath = javaTutorPlusWorkingDir +str(get_exec.id)


  
  if not os.path.exists(currentTargetPath):
     print >> sys.stderr, 'Making dir'
     os.makedirs(currentTargetPath)
  else:   
     print >> sys.stderr, 'removing contents'
     for root, dirs, files in os.walk(currentTargetPath):
        for f in files:
          os.unlink(os.path.join(root, f))
        for d in dirs:
          shutil.rmtree(os.path.join(root, d))

  

  #We finst need to find the name of the class file to store it as a .java file
  words = source.split()

  found = False
  name = "Unknown"
  for word in words:
      if found :
          name = word
          break;
      if word == 'class':
          found = True

  if '{' in name:
    name = name[:-1]

  targetFilePath = currentTargetPath + '/' + name + '.java';
  print targetFilePath

  
  

  tempFile = open(targetFilePath, 'w')
  tempFile.write(source);
  tempFile.close()

  print targetFilePath



#  call(['java', '-jar', '/Users/hauswirm/tools/extendj/extendj.jar','-g', targetFilePath])
#  try:
#    check_call(['/bin/bash', '-i', '-c','extendj '+ targetFilePath])
#  except CalledProcessError as e:
#    print >>sys.stderr, "Compilation failed:", e

  blastOp='-Dblast=/Users/hauswirm/tools/informationflowtracer'
  extendjOp='-Dextendj=/Users/hauswirm/tools/extendj/extendj.jar'
  pythonTutorOp='-Dpython-tutor=/Users/hauswirm/tools/OnlinePythonTutor'
  
  extendjArgs = "-Dextendj-args='-d " + currentTargetPath + " " + targetFilePath+ "'";
  print extendjArgs
  fileNameArg = '-DfileName=' + name
  print fileNameArg
  requestIdArg = '-DrequestId=' + str(get_exec.id)
  print requestIdArg
  
  call(["ant", "-f", "/Users/hauswirm/tools/java-tutor-plus", "run", extendjArgs, fileNameArg, requestIdArg, blastOp, extendjOp, pythonTutorOp])
#  return static_file("test.json", root=javaTutorPlusWorkingDir)
  return static_file("data.json", root=currentTargetPath)

@get('/load_matrix_problem.py')
def load_matrix_problem():
  print >> sys.stderr, '5'
  prob_name = request.query.problem_name
  assert type(prob_name) in (str, unicode)

  # whitelist
  assert prob_name in ('python_comprehension-1',)

  fn = 'matrix-demo/' + prob_name + '.py'
  f = open(fn)
  cod = f.read()
  f.close()

  import doctest
  import sys
  p = doctest.DocTestParser()
  examples = p.get_examples(cod)
  if len(examples):
    first_ex = examples[0]
    #print >> sys.stderr, 'Source:', `first_ex.source`
    testCod = 'result = ' + first_ex.source

  return json.dumps(dict(code=cod, test=testCod))


get_exec.id = 1

@get('/submit_matrix_problem.py')
def submit_matrix_problem():
  print >> sys.stderr, '6'
  user_code = request.query.submitted_code
  prob_name = request.query.problem_name
  assert type(prob_name) in (str, unicode)

  # whitelist
  assert prob_name in ('python_comprehension-1',)

  test_fn = 'matrix-demo/' + prob_name + '.test.py'
  test_cod = open(test_fn).read()

  # concatenate!
  script = test_cod + '\n' + user_code + \
'''
import doctest
(n_fail, n_tests) = doctest.testmod(verbose=False)
if n_fail == 0:
    print("All %d tests passed!" % n_tests)
'''

  url = 'http://ec2-107-20-94-197.compute-1.amazonaws.com/cgi-bin/run_code.py'
  values = {'user_script' : script}

  data = urllib.urlencode(values)
  req = urllib2.Request(url, data)
  response = urllib2.urlopen(req)
  the_page = response.read()
  return the_page


if __name__ == "__main__":
    #run(host='localhost', port=8080, reloader=True)
    run(host='0.0.0.0', port=8003, reloader=True) # make it externally visible - DANGER this is very insecure since there's no sandboxing!
