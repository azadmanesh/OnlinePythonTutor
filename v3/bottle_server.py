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

wd = 'wd'
blast_opt_frontend = '/Users/reza/PhD/tools/OnlinePythonTutor/v3'
junit_class_path = 'blast-opt-backend/lib/junit-4.11.jar:blast-opt-backend/lib/hamcrest-core-1.3.jar'
blast_opt_controller = '/Users/reza/Documents/workspace2/blast-opt-controller'
blast_class_path = '/Users/reza/tempInfoFlow/informationflowtracer/build/main'

inlined_test_class_name = 'InlinedTest'
inlined_test_method_name = 'test'

#AZM: Launching BLAST for different input cases
def executeInlinedTest():
    print "[execute]:\tInlined Test"
    print request.query.user_script
    
    currentRelativeTargetPath = ''+ wd + '/' + 's' + request.query.session_uuid
      
    if not os.path.exists(currentRelativeTargetPath):
        print >> sys.stderr, 'Making dir'
        os.makedirs(currentRelativeTargetPath)
    
    targetFilePath = currentRelativeTargetPath + '/' + 'InlinedTest.java';
    
    tempFile = open(targetFilePath, 'w')
    tempFile.write(request.query.user_script);
    tempFile.close()
    
    classpath = junit_class_path
    print 'classpath:\t' + classpath
    
    retcode = call(["javac", "-g","-cp", classpath, "-source", "7", "-target", "7", targetFilePath])
    
    if retcode != 0:
        raise Exception("Compile error!")
    
    print 'test.class.name:\t' + inlined_test_class_name
    print 'test.method.name:\t' + inlined_test_method_name
    print 'target.wd:\t' + blast_opt_frontend+ '/' + currentRelativeTargetPath
    
    retcode = call(["ant", "-f", blast_opt_controller, 
                                                    "run", 
                                                    "-Dtest.class="+inlined_test_class_name,
                                                    "-Dtest.method="+inlined_test_method_name,
                                                    "-Dtarget=" + blast_opt_frontend+ '/' + currentRelativeTargetPath])
    print "opt-controller:\t" + str(retcode)
    if retcode != 0:
        raise Exception("Opt-controller failed in running the test!")
    
    print 'Results written into:\t' + blast_opt_frontend+ '/' + currentRelativeTargetPath
    
    return static_file("data.json", root=currentRelativeTargetPath)

def executeJarFile():
    print "executing jar file"
    
def executeCustomAddress():
    print "executing custom address"

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

@get('/query')
def exec_query():
    print "Execute query in server side!!!!"
    currentRelativeTargetPath = ''+ wd + '/' + 's' + request.query.session_uuid;
    print currentRelativeTargetPath;
    
    
    if not os.path.exists(currentRelativeTargetPath):
        raise Exception('Invalid session! Not found any match for the given session!')
    
    queryFilePath = currentRelativeTargetPath + '/' + 'Query.java';
    
    tempFile = open(queryFilePath, 'w')
    tempFile.write(request.query.query_to_execute)
    tempFile.close()
    
    retcode = call(["javac", "-g","-cp", blast_class_path, "-source", "7", "-target", "7", queryFilePath])
    
    if retcode != 0:
        raise Exception("Compile error!")
    
    print 'working directory:\t' + currentRelativeTargetPath
    

@get('/exec')
def get_exec():
    print "Hi from Python"
    print >> sys.stderr, 'ID:' 
    print "classpath:\t" + request.query.class_path
    print >> sys.stderr, get_exec.id
    print "sessionId:\t" + request.query.session_uuid
    
    input_type = request.query.input_type
    if input_type == 'inlineTestPane':
        return executeInlinedTest()
    elif input_type == 'jarFilePane':
        return executeJarFile()
    elif input_type == 'customAddressPane':
        return executeCustomAddress()
    else:
        raise Exception('Invalid input type specified!')   
       
    
    

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
    
