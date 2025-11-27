var Vr = { exports: {} }, Xe = {}, zr = { exports: {} }, b = {};
/**
 * @license React
 * react.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
var vt;
function Wt() {
  if (vt) return b;
  vt = 1;
  var k = Symbol.for("react.element"), l = Symbol.for("react.portal"), M = Symbol.for("react.fragment"), q = Symbol.for("react.strict_mode"), ie = Symbol.for("react.profiler"), X = Symbol.for("react.provider"), B = Symbol.for("react.context"), U = Symbol.for("react.forward_ref"), F = Symbol.for("react.suspense"), G = Symbol.for("react.memo"), R = Symbol.for("react.lazy"), $ = Symbol.iterator;
  function x(n) {
    return n === null || typeof n != "object" ? null : (n = $ && n[$] || n["@@iterator"], typeof n == "function" ? n : null);
  }
  var I = { isMounted: function() {
    return !1;
  }, enqueueForceUpdate: function() {
  }, enqueueReplaceState: function() {
  }, enqueueSetState: function() {
  } }, ue = Object.assign, ye = {};
  function ne(n, u, C) {
    this.props = n, this.context = u, this.refs = ye, this.updater = C || I;
  }
  ne.prototype.isReactComponent = {}, ne.prototype.setState = function(n, u) {
    if (typeof n != "object" && typeof n != "function" && n != null) throw Error("setState(...): takes an object of state variables to update or a function which returns an object of state variables.");
    this.updater.enqueueSetState(this, n, u, "setState");
  }, ne.prototype.forceUpdate = function(n) {
    this.updater.enqueueForceUpdate(this, n, "forceUpdate");
  };
  function le() {
  }
  le.prototype = ne.prototype;
  function E(n, u, C) {
    this.props = n, this.context = u, this.refs = ye, this.updater = C || I;
  }
  var ee = E.prototype = new le();
  ee.constructor = E, ue(ee, ne.prototype), ee.isPureReactComponent = !0;
  var g = Array.isArray, v = Object.prototype.hasOwnProperty, S = { current: null }, J = { key: !0, ref: !0, __self: !0, __source: !0 };
  function se(n, u, C) {
    var A, P = {}, z = null, W = null;
    if (u != null) for (A in u.ref !== void 0 && (W = u.ref), u.key !== void 0 && (z = "" + u.key), u) v.call(u, A) && !J.hasOwnProperty(A) && (P[A] = u[A]);
    var L = arguments.length - 2;
    if (L === 1) P.children = C;
    else if (1 < L) {
      for (var N = Array(L), ae = 0; ae < L; ae++) N[ae] = arguments[ae + 2];
      P.children = N;
    }
    if (n && n.defaultProps) for (A in L = n.defaultProps, L) P[A] === void 0 && (P[A] = L[A]);
    return { $$typeof: k, type: n, key: z, ref: W, props: P, _owner: S.current };
  }
  function ce(n, u) {
    return { $$typeof: k, type: n.type, key: u, ref: n.ref, props: n.props, _owner: n._owner };
  }
  function fe(n) {
    return typeof n == "object" && n !== null && n.$$typeof === k;
  }
  function Ee(n) {
    var u = { "=": "=0", ":": "=2" };
    return "$" + n.replace(/[=:]/g, function(C) {
      return u[C];
    });
  }
  var p = /\/+/g;
  function Y(n, u) {
    return typeof n == "object" && n !== null && n.key != null ? Ee("" + n.key) : u.toString(36);
  }
  function te(n, u, C, A, P) {
    var z = typeof n;
    (z === "undefined" || z === "boolean") && (n = null);
    var W = !1;
    if (n === null) W = !0;
    else switch (z) {
      case "string":
      case "number":
        W = !0;
        break;
      case "object":
        switch (n.$$typeof) {
          case k:
          case l:
            W = !0;
        }
    }
    if (W) return W = n, P = P(W), n = A === "" ? "." + Y(W, 0) : A, g(P) ? (C = "", n != null && (C = n.replace(p, "$&/") + "/"), te(P, u, C, "", function(ae) {
      return ae;
    })) : P != null && (fe(P) && (P = ce(P, C + (!P.key || W && W.key === P.key ? "" : ("" + P.key).replace(p, "$&/") + "/") + n)), u.push(P)), 1;
    if (W = 0, A = A === "" ? "." : A + ":", g(n)) for (var L = 0; L < n.length; L++) {
      z = n[L];
      var N = A + Y(z, L);
      W += te(z, u, C, N, P);
    }
    else if (N = x(n), typeof N == "function") for (n = N.call(n), L = 0; !(z = n.next()).done; ) z = z.value, N = A + Y(z, L++), W += te(z, u, C, N, P);
    else if (z === "object") throw u = String(n), Error("Objects are not valid as a React child (found: " + (u === "[object Object]" ? "object with keys {" + Object.keys(n).join(", ") + "}" : u) + "). If you meant to render a collection of children, use an array instead.");
    return W;
  }
  function de(n, u, C) {
    if (n == null) return n;
    var A = [], P = 0;
    return te(n, A, "", "", function(z) {
      return u.call(C, z, P++);
    }), A;
  }
  function _e(n) {
    if (n._status === -1) {
      var u = n._result;
      u = u(), u.then(function(C) {
        (n._status === 0 || n._status === -1) && (n._status = 1, n._result = C);
      }, function(C) {
        (n._status === 0 || n._status === -1) && (n._status = 2, n._result = C);
      }), n._status === -1 && (n._status = 0, n._result = u);
    }
    if (n._status === 1) return n._result.default;
    throw n._result;
  }
  var h = { current: null }, be = { transition: null }, Pe = { ReactCurrentDispatcher: h, ReactCurrentBatchConfig: be, ReactCurrentOwner: S };
  function Se() {
    throw Error("act(...) is not supported in production builds of React.");
  }
  return b.Children = { map: de, forEach: function(n, u, C) {
    de(n, function() {
      u.apply(this, arguments);
    }, C);
  }, count: function(n) {
    var u = 0;
    return de(n, function() {
      u++;
    }), u;
  }, toArray: function(n) {
    return de(n, function(u) {
      return u;
    }) || [];
  }, only: function(n) {
    if (!fe(n)) throw Error("React.Children.only expected to receive a single React element child.");
    return n;
  } }, b.Component = ne, b.Fragment = M, b.Profiler = ie, b.PureComponent = E, b.StrictMode = q, b.Suspense = F, b.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED = Pe, b.act = Se, b.cloneElement = function(n, u, C) {
    if (n == null) throw Error("React.cloneElement(...): The argument must be a React element, but you passed " + n + ".");
    var A = ue({}, n.props), P = n.key, z = n.ref, W = n._owner;
    if (u != null) {
      if (u.ref !== void 0 && (z = u.ref, W = S.current), u.key !== void 0 && (P = "" + u.key), n.type && n.type.defaultProps) var L = n.type.defaultProps;
      for (N in u) v.call(u, N) && !J.hasOwnProperty(N) && (A[N] = u[N] === void 0 && L !== void 0 ? L[N] : u[N]);
    }
    var N = arguments.length - 2;
    if (N === 1) A.children = C;
    else if (1 < N) {
      L = Array(N);
      for (var ae = 0; ae < N; ae++) L[ae] = arguments[ae + 2];
      A.children = L;
    }
    return { $$typeof: k, type: n.type, key: P, ref: z, props: A, _owner: W };
  }, b.createContext = function(n) {
    return n = { $$typeof: B, _currentValue: n, _currentValue2: n, _threadCount: 0, Provider: null, Consumer: null, _defaultValue: null, _globalName: null }, n.Provider = { $$typeof: X, _context: n }, n.Consumer = n;
  }, b.createElement = se, b.createFactory = function(n) {
    var u = se.bind(null, n);
    return u.type = n, u;
  }, b.createRef = function() {
    return { current: null };
  }, b.forwardRef = function(n) {
    return { $$typeof: U, render: n };
  }, b.isValidElement = fe, b.lazy = function(n) {
    return { $$typeof: R, _payload: { _status: -1, _result: n }, _init: _e };
  }, b.memo = function(n, u) {
    return { $$typeof: G, type: n, compare: u === void 0 ? null : u };
  }, b.startTransition = function(n) {
    var u = be.transition;
    be.transition = {};
    try {
      n();
    } finally {
      be.transition = u;
    }
  }, b.unstable_act = Se, b.useCallback = function(n, u) {
    return h.current.useCallback(n, u);
  }, b.useContext = function(n) {
    return h.current.useContext(n);
  }, b.useDebugValue = function() {
  }, b.useDeferredValue = function(n) {
    return h.current.useDeferredValue(n);
  }, b.useEffect = function(n, u) {
    return h.current.useEffect(n, u);
  }, b.useId = function() {
    return h.current.useId();
  }, b.useImperativeHandle = function(n, u, C) {
    return h.current.useImperativeHandle(n, u, C);
  }, b.useInsertionEffect = function(n, u) {
    return h.current.useInsertionEffect(n, u);
  }, b.useLayoutEffect = function(n, u) {
    return h.current.useLayoutEffect(n, u);
  }, b.useMemo = function(n, u) {
    return h.current.useMemo(n, u);
  }, b.useReducer = function(n, u, C) {
    return h.current.useReducer(n, u, C);
  }, b.useRef = function(n) {
    return h.current.useRef(n);
  }, b.useState = function(n) {
    return h.current.useState(n);
  }, b.useSyncExternalStore = function(n, u, C) {
    return h.current.useSyncExternalStore(n, u, C);
  }, b.useTransition = function() {
    return h.current.useTransition();
  }, b.version = "18.3.1", b;
}
var rr = { exports: {} };
/**
 * @license React
 * react.development.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
rr.exports;
var ht;
function Vt() {
  return ht || (ht = 1, function(k, l) {
    process.env.NODE_ENV !== "production" && function() {
      typeof __REACT_DEVTOOLS_GLOBAL_HOOK__ < "u" && typeof __REACT_DEVTOOLS_GLOBAL_HOOK__.registerInternalModuleStart == "function" && __REACT_DEVTOOLS_GLOBAL_HOOK__.registerInternalModuleStart(new Error());
      var M = "18.3.1", q = Symbol.for("react.element"), ie = Symbol.for("react.portal"), X = Symbol.for("react.fragment"), B = Symbol.for("react.strict_mode"), U = Symbol.for("react.profiler"), F = Symbol.for("react.provider"), G = Symbol.for("react.context"), R = Symbol.for("react.forward_ref"), $ = Symbol.for("react.suspense"), x = Symbol.for("react.suspense_list"), I = Symbol.for("react.memo"), ue = Symbol.for("react.lazy"), ye = Symbol.for("react.offscreen"), ne = Symbol.iterator, le = "@@iterator";
      function E(e) {
        if (e === null || typeof e != "object")
          return null;
        var r = ne && e[ne] || e[le];
        return typeof r == "function" ? r : null;
      }
      var ee = {
        /**
         * @internal
         * @type {ReactComponent}
         */
        current: null
      }, g = {
        transition: null
      }, v = {
        current: null,
        // Used to reproduce behavior of `batchedUpdates` in legacy mode.
        isBatchingLegacy: !1,
        didScheduleLegacyUpdate: !1
      }, S = {
        /**
         * @internal
         * @type {ReactComponent}
         */
        current: null
      }, J = {}, se = null;
      function ce(e) {
        se = e;
      }
      J.setExtraStackFrame = function(e) {
        se = e;
      }, J.getCurrentStack = null, J.getStackAddendum = function() {
        var e = "";
        se && (e += se);
        var r = J.getCurrentStack;
        return r && (e += r() || ""), e;
      };
      var fe = !1, Ee = !1, p = !1, Y = !1, te = !1, de = {
        ReactCurrentDispatcher: ee,
        ReactCurrentBatchConfig: g,
        ReactCurrentOwner: S
      };
      de.ReactDebugCurrentFrame = J, de.ReactCurrentActQueue = v;
      function _e(e) {
        {
          for (var r = arguments.length, a = new Array(r > 1 ? r - 1 : 0), o = 1; o < r; o++)
            a[o - 1] = arguments[o];
          be("warn", e, a);
        }
      }
      function h(e) {
        {
          for (var r = arguments.length, a = new Array(r > 1 ? r - 1 : 0), o = 1; o < r; o++)
            a[o - 1] = arguments[o];
          be("error", e, a);
        }
      }
      function be(e, r, a) {
        {
          var o = de.ReactDebugCurrentFrame, s = o.getStackAddendum();
          s !== "" && (r += "%s", a = a.concat([s]));
          var y = a.map(function(d) {
            return String(d);
          });
          y.unshift("Warning: " + r), Function.prototype.apply.call(console[e], console, y);
        }
      }
      var Pe = {};
      function Se(e, r) {
        {
          var a = e.constructor, o = a && (a.displayName || a.name) || "ReactClass", s = o + "." + r;
          if (Pe[s])
            return;
          h("Can't call %s on a component that is not yet mounted. This is a no-op, but it might indicate a bug in your application. Instead, assign to `this.state` directly or define a `state = {};` class property with the desired state in the %s component.", r, o), Pe[s] = !0;
        }
      }
      var n = {
        /**
         * Checks whether or not this composite component is mounted.
         * @param {ReactClass} publicInstance The instance we want to test.
         * @return {boolean} True if mounted, false otherwise.
         * @protected
         * @final
         */
        isMounted: function(e) {
          return !1;
        },
        /**
         * Forces an update. This should only be invoked when it is known with
         * certainty that we are **not** in a DOM transaction.
         *
         * You may want to call this when you know that some deeper aspect of the
         * component's state has changed but `setState` was not called.
         *
         * This will not invoke `shouldComponentUpdate`, but it will invoke
         * `componentWillUpdate` and `componentDidUpdate`.
         *
         * @param {ReactClass} publicInstance The instance that should rerender.
         * @param {?function} callback Called after component is updated.
         * @param {?string} callerName name of the calling function in the public API.
         * @internal
         */
        enqueueForceUpdate: function(e, r, a) {
          Se(e, "forceUpdate");
        },
        /**
         * Replaces all of the state. Always use this or `setState` to mutate state.
         * You should treat `this.state` as immutable.
         *
         * There is no guarantee that `this.state` will be immediately updated, so
         * accessing `this.state` after calling this method may return the old value.
         *
         * @param {ReactClass} publicInstance The instance that should rerender.
         * @param {object} completeState Next state.
         * @param {?function} callback Called after component is updated.
         * @param {?string} callerName name of the calling function in the public API.
         * @internal
         */
        enqueueReplaceState: function(e, r, a, o) {
          Se(e, "replaceState");
        },
        /**
         * Sets a subset of the state. This only exists because _pendingState is
         * internal. This provides a merging strategy that is not available to deep
         * properties which is confusing. TODO: Expose pendingState or don't use it
         * during the merge.
         *
         * @param {ReactClass} publicInstance The instance that should rerender.
         * @param {object} partialState Next partial state to be merged with state.
         * @param {?function} callback Called after component is updated.
         * @param {?string} Name of the calling function in the public API.
         * @internal
         */
        enqueueSetState: function(e, r, a, o) {
          Se(e, "setState");
        }
      }, u = Object.assign, C = {};
      Object.freeze(C);
      function A(e, r, a) {
        this.props = e, this.context = r, this.refs = C, this.updater = a || n;
      }
      A.prototype.isReactComponent = {}, A.prototype.setState = function(e, r) {
        if (typeof e != "object" && typeof e != "function" && e != null)
          throw new Error("setState(...): takes an object of state variables to update or a function which returns an object of state variables.");
        this.updater.enqueueSetState(this, e, r, "setState");
      }, A.prototype.forceUpdate = function(e) {
        this.updater.enqueueForceUpdate(this, e, "forceUpdate");
      };
      {
        var P = {
          isMounted: ["isMounted", "Instead, make sure to clean up subscriptions and pending requests in componentWillUnmount to prevent memory leaks."],
          replaceState: ["replaceState", "Refactor your code to use setState instead (see https://github.com/facebook/react/issues/3236)."]
        }, z = function(e, r) {
          Object.defineProperty(A.prototype, e, {
            get: function() {
              _e("%s(...) is deprecated in plain JavaScript React classes. %s", r[0], r[1]);
            }
          });
        };
        for (var W in P)
          P.hasOwnProperty(W) && z(W, P[W]);
      }
      function L() {
      }
      L.prototype = A.prototype;
      function N(e, r, a) {
        this.props = e, this.context = r, this.refs = C, this.updater = a || n;
      }
      var ae = N.prototype = new L();
      ae.constructor = N, u(ae, A.prototype), ae.isPureReactComponent = !0;
      function Er() {
        var e = {
          current: null
        };
        return Object.seal(e), e;
      }
      var tr = Array.isArray;
      function Ue(e) {
        return tr(e);
      }
      function _r(e) {
        {
          var r = typeof Symbol == "function" && Symbol.toStringTag, a = r && e[Symbol.toStringTag] || e.constructor.name || "Object";
          return a;
        }
      }
      function $e(e) {
        try {
          return we(e), !1;
        } catch {
          return !0;
        }
      }
      function we(e) {
        return "" + e;
      }
      function je(e) {
        if ($e(e))
          return h("The provided key is an unsupported type %s. This value must be coerced to a string before before using it here.", _r(e)), we(e);
      }
      function nr(e, r, a) {
        var o = e.displayName;
        if (o)
          return o;
        var s = r.displayName || r.name || "";
        return s !== "" ? a + "(" + s + ")" : a;
      }
      function ke(e) {
        return e.displayName || "Context";
      }
      function Re(e) {
        if (e == null)
          return null;
        if (typeof e.tag == "number" && h("Received an unexpected object in getComponentNameFromType(). This is likely a bug in React. Please file an issue."), typeof e == "function")
          return e.displayName || e.name || null;
        if (typeof e == "string")
          return e;
        switch (e) {
          case X:
            return "Fragment";
          case ie:
            return "Portal";
          case U:
            return "Profiler";
          case B:
            return "StrictMode";
          case $:
            return "Suspense";
          case x:
            return "SuspenseList";
        }
        if (typeof e == "object")
          switch (e.$$typeof) {
            case G:
              var r = e;
              return ke(r) + ".Consumer";
            case F:
              var a = e;
              return ke(a._context) + ".Provider";
            case R:
              return nr(e, e.render, "ForwardRef");
            case I:
              var o = e.displayName || null;
              return o !== null ? o : Re(e.type) || "Memo";
            case ue: {
              var s = e, y = s._payload, d = s._init;
              try {
                return Re(d(y));
              } catch {
                return null;
              }
            }
          }
        return null;
      }
      var Fe = Object.prototype.hasOwnProperty, Ie = {
        key: !0,
        ref: !0,
        __self: !0,
        __source: !0
      }, ar, or, Le;
      Le = {};
      function Be(e) {
        if (Fe.call(e, "ref")) {
          var r = Object.getOwnPropertyDescriptor(e, "ref").get;
          if (r && r.isReactWarning)
            return !1;
        }
        return e.ref !== void 0;
      }
      function Ge(e) {
        if (Fe.call(e, "key")) {
          var r = Object.getOwnPropertyDescriptor(e, "key").get;
          if (r && r.isReactWarning)
            return !1;
        }
        return e.key !== void 0;
      }
      function br(e, r) {
        var a = function() {
          ar || (ar = !0, h("%s: `key` is not a prop. Trying to access it will result in `undefined` being returned. If you need to access the same value within the child component, you should pass it as a different prop. (https://reactjs.org/link/special-props)", r));
        };
        a.isReactWarning = !0, Object.defineProperty(e, "key", {
          get: a,
          configurable: !0
        });
      }
      function ir(e, r) {
        var a = function() {
          or || (or = !0, h("%s: `ref` is not a prop. Trying to access it will result in `undefined` being returned. If you need to access the same value within the child component, you should pass it as a different prop. (https://reactjs.org/link/special-props)", r));
        };
        a.isReactWarning = !0, Object.defineProperty(e, "ref", {
          get: a,
          configurable: !0
        });
      }
      function ur(e) {
        if (typeof e.ref == "string" && S.current && e.__self && S.current.stateNode !== e.__self) {
          var r = Re(S.current.type);
          Le[r] || (h('Component "%s" contains the string ref "%s". Support for string refs will be removed in a future major release. This case cannot be automatically converted to an arrow function. We ask you to manually fix this case by using useRef() or createRef() instead. Learn more about using refs safely here: https://reactjs.org/link/strict-mode-string-ref', r, e.ref), Le[r] = !0);
        }
      }
      var Ke = function(e, r, a, o, s, y, d) {
        var m = {
          // This tag allows us to uniquely identify this as a React Element
          $$typeof: q,
          // Built-in properties that belong on the element
          type: e,
          key: r,
          ref: a,
          props: d,
          // Record the component responsible for creating this element.
          _owner: y
        };
        return m._store = {}, Object.defineProperty(m._store, "validated", {
          configurable: !1,
          enumerable: !1,
          writable: !0,
          value: !1
        }), Object.defineProperty(m, "_self", {
          configurable: !1,
          enumerable: !1,
          writable: !1,
          value: o
        }), Object.defineProperty(m, "_source", {
          configurable: !1,
          enumerable: !1,
          writable: !1,
          value: s
        }), Object.freeze && (Object.freeze(m.props), Object.freeze(m)), m;
      };
      function Rr(e, r, a) {
        var o, s = {}, y = null, d = null, m = null, O = null;
        if (r != null) {
          Be(r) && (d = r.ref, ur(r)), Ge(r) && (je(r.key), y = "" + r.key), m = r.__self === void 0 ? null : r.__self, O = r.__source === void 0 ? null : r.__source;
          for (o in r)
            Fe.call(r, o) && !Ie.hasOwnProperty(o) && (s[o] = r[o]);
        }
        var V = arguments.length - 2;
        if (V === 1)
          s.children = a;
        else if (V > 1) {
          for (var K = Array(V), H = 0; H < V; H++)
            K[H] = arguments[H + 2];
          Object.freeze && Object.freeze(K), s.children = K;
        }
        if (e && e.defaultProps) {
          var Q = e.defaultProps;
          for (o in Q)
            s[o] === void 0 && (s[o] = Q[o]);
        }
        if (y || d) {
          var oe = typeof e == "function" ? e.displayName || e.name || "Unknown" : e;
          y && br(s, oe), d && ir(s, oe);
        }
        return Ke(e, y, d, m, O, S.current, s);
      }
      function Sr(e, r) {
        var a = Ke(e.type, r, e.ref, e._self, e._source, e._owner, e.props);
        return a;
      }
      function Cr(e, r, a) {
        if (e == null)
          throw new Error("React.cloneElement(...): The argument must be a React element, but you passed " + e + ".");
        var o, s = u({}, e.props), y = e.key, d = e.ref, m = e._self, O = e._source, V = e._owner;
        if (r != null) {
          Be(r) && (d = r.ref, V = S.current), Ge(r) && (je(r.key), y = "" + r.key);
          var K;
          e.type && e.type.defaultProps && (K = e.type.defaultProps);
          for (o in r)
            Fe.call(r, o) && !Ie.hasOwnProperty(o) && (r[o] === void 0 && K !== void 0 ? s[o] = K[o] : s[o] = r[o]);
        }
        var H = arguments.length - 2;
        if (H === 1)
          s.children = a;
        else if (H > 1) {
          for (var Q = Array(H), oe = 0; oe < H; oe++)
            Q[oe] = arguments[oe + 2];
          s.children = Q;
        }
        return Ke(e.type, y, d, m, O, V, s);
      }
      function Ae(e) {
        return typeof e == "object" && e !== null && e.$$typeof === q;
      }
      var sr = ".", wr = ":";
      function He(e) {
        var r = /[=:]/g, a = {
          "=": "=0",
          ":": "=2"
        }, o = e.replace(r, function(s) {
          return a[s];
        });
        return "$" + o;
      }
      var qe = !1, Te = /\/+/g;
      function Me(e) {
        return e.replace(Te, "$&/");
      }
      function xe(e, r) {
        return typeof e == "object" && e !== null && e.key != null ? (je(e.key), He("" + e.key)) : r.toString(36);
      }
      function Ne(e, r, a, o, s) {
        var y = typeof e;
        (y === "undefined" || y === "boolean") && (e = null);
        var d = !1;
        if (e === null)
          d = !0;
        else
          switch (y) {
            case "string":
            case "number":
              d = !0;
              break;
            case "object":
              switch (e.$$typeof) {
                case q:
                case ie:
                  d = !0;
              }
          }
        if (d) {
          var m = e, O = s(m), V = o === "" ? sr + xe(m, 0) : o;
          if (Ue(O)) {
            var K = "";
            V != null && (K = Me(V) + "/"), Ne(O, r, K, "", function(Mt) {
              return Mt;
            });
          } else O != null && (Ae(O) && (O.key && (!m || m.key !== O.key) && je(O.key), O = Sr(
            O,
            // Keep both the (mapped) and old keys if they differ, just as
            // traverseAllChildren used to do for objects as children
            a + // $FlowFixMe Flow incorrectly thinks React.Portal doesn't have a key
            (O.key && (!m || m.key !== O.key) ? (
              // $FlowFixMe Flow incorrectly thinks existing element's key can be a number
              // eslint-disable-next-line react-internal/safe-string-coercion
              Me("" + O.key) + "/"
            ) : "") + V
          )), r.push(O));
          return 1;
        }
        var H, Q, oe = 0, ve = o === "" ? sr : o + wr;
        if (Ue(e))
          for (var gr = 0; gr < e.length; gr++)
            H = e[gr], Q = ve + xe(H, gr), oe += Ne(H, r, a, Q, s);
        else {
          var Wr = E(e);
          if (typeof Wr == "function") {
            var ft = e;
            Wr === ft.entries && (qe || _e("Using Maps as children is not supported. Use an array of keyed ReactElements instead."), qe = !0);
            for (var It = Wr.call(ft), dt, Lt = 0; !(dt = It.next()).done; )
              H = dt.value, Q = ve + xe(H, Lt++), oe += Ne(H, r, a, Q, s);
          } else if (y === "object") {
            var pt = String(e);
            throw new Error("Objects are not valid as a React child (found: " + (pt === "[object Object]" ? "object with keys {" + Object.keys(e).join(", ") + "}" : pt) + "). If you meant to render a collection of children, use an array instead.");
          }
        }
        return oe;
      }
      function We(e, r, a) {
        if (e == null)
          return e;
        var o = [], s = 0;
        return Ne(e, o, "", "", function(y) {
          return r.call(a, y, s++);
        }), o;
      }
      function cr(e) {
        var r = 0;
        return We(e, function() {
          r++;
        }), r;
      }
      function Ar(e, r, a) {
        We(e, function() {
          r.apply(this, arguments);
        }, a);
      }
      function lr(e) {
        return We(e, function(r) {
          return r;
        }) || [];
      }
      function fr(e) {
        if (!Ae(e))
          throw new Error("React.Children.only expected to receive a single React element child.");
        return e;
      }
      function Tr(e) {
        var r = {
          $$typeof: G,
          // As a workaround to support multiple concurrent renderers, we categorize
          // some renderers as primary and others as secondary. We only expect
          // there to be two concurrent renderers at most: React Native (primary) and
          // Fabric (secondary); React DOM (primary) and React ART (secondary).
          // Secondary renderers store their context values on separate fields.
          _currentValue: e,
          _currentValue2: e,
          // Used to track how many concurrent renderers this context currently
          // supports within in a single renderer. Such as parallel server rendering.
          _threadCount: 0,
          // These are circular
          Provider: null,
          Consumer: null,
          // Add these to use same hidden class in VM as ServerContext
          _defaultValue: null,
          _globalName: null
        };
        r.Provider = {
          $$typeof: F,
          _context: r
        };
        var a = !1, o = !1, s = !1;
        {
          var y = {
            $$typeof: G,
            _context: r
          };
          Object.defineProperties(y, {
            Provider: {
              get: function() {
                return o || (o = !0, h("Rendering <Context.Consumer.Provider> is not supported and will be removed in a future major release. Did you mean to render <Context.Provider> instead?")), r.Provider;
              },
              set: function(d) {
                r.Provider = d;
              }
            },
            _currentValue: {
              get: function() {
                return r._currentValue;
              },
              set: function(d) {
                r._currentValue = d;
              }
            },
            _currentValue2: {
              get: function() {
                return r._currentValue2;
              },
              set: function(d) {
                r._currentValue2 = d;
              }
            },
            _threadCount: {
              get: function() {
                return r._threadCount;
              },
              set: function(d) {
                r._threadCount = d;
              }
            },
            Consumer: {
              get: function() {
                return a || (a = !0, h("Rendering <Context.Consumer.Consumer> is not supported and will be removed in a future major release. Did you mean to render <Context.Consumer> instead?")), r.Consumer;
              }
            },
            displayName: {
              get: function() {
                return r.displayName;
              },
              set: function(d) {
                s || (_e("Setting `displayName` on Context.Consumer has no effect. You should set it directly on the context with Context.displayName = '%s'.", d), s = !0);
              }
            }
          }), r.Consumer = y;
        }
        return r._currentRenderer = null, r._currentRenderer2 = null, r;
      }
      var De = -1, Ve = 0, Je = 1, Or = 2;
      function Pr(e) {
        if (e._status === De) {
          var r = e._result, a = r();
          if (a.then(function(y) {
            if (e._status === Ve || e._status === De) {
              var d = e;
              d._status = Je, d._result = y;
            }
          }, function(y) {
            if (e._status === Ve || e._status === De) {
              var d = e;
              d._status = Or, d._result = y;
            }
          }), e._status === De) {
            var o = e;
            o._status = Ve, o._result = a;
          }
        }
        if (e._status === Je) {
          var s = e._result;
          return s === void 0 && h(`lazy: Expected the result of a dynamic import() call. Instead received: %s

Your code should look like: 
  const MyComponent = lazy(() => import('./MyComponent'))

Did you accidentally put curly braces around the import?`, s), "default" in s || h(`lazy: Expected the result of a dynamic import() call. Instead received: %s

Your code should look like: 
  const MyComponent = lazy(() => import('./MyComponent'))`, s), s.default;
        } else
          throw e._result;
      }
      function jr(e) {
        var r = {
          // We use these fields to store the result.
          _status: De,
          _result: e
        }, a = {
          $$typeof: ue,
          _payload: r,
          _init: Pr
        };
        {
          var o, s;
          Object.defineProperties(a, {
            defaultProps: {
              configurable: !0,
              get: function() {
                return o;
              },
              set: function(y) {
                h("React.lazy(...): It is not supported to assign `defaultProps` to a lazy component import. Either specify them where the component is defined, or create a wrapping component around it."), o = y, Object.defineProperty(a, "defaultProps", {
                  enumerable: !0
                });
              }
            },
            propTypes: {
              configurable: !0,
              get: function() {
                return s;
              },
              set: function(y) {
                h("React.lazy(...): It is not supported to assign `propTypes` to a lazy component import. Either specify them where the component is defined, or create a wrapping component around it."), s = y, Object.defineProperty(a, "propTypes", {
                  enumerable: !0
                });
              }
            }
          });
        }
        return a;
      }
      function kr(e) {
        e != null && e.$$typeof === I ? h("forwardRef requires a render function but received a `memo` component. Instead of forwardRef(memo(...)), use memo(forwardRef(...)).") : typeof e != "function" ? h("forwardRef requires a render function but was given %s.", e === null ? "null" : typeof e) : e.length !== 0 && e.length !== 2 && h("forwardRef render functions accept exactly two parameters: props and ref. %s", e.length === 1 ? "Did you forget to use the ref parameter?" : "Any additional parameter will be undefined."), e != null && (e.defaultProps != null || e.propTypes != null) && h("forwardRef render functions do not support propTypes or defaultProps. Did you accidentally pass a React component?");
        var r = {
          $$typeof: R,
          render: e
        };
        {
          var a;
          Object.defineProperty(r, "displayName", {
            enumerable: !1,
            configurable: !0,
            get: function() {
              return a;
            },
            set: function(o) {
              a = o, !e.name && !e.displayName && (e.displayName = o);
            }
          });
        }
        return r;
      }
      var t;
      t = Symbol.for("react.module.reference");
      function i(e) {
        return !!(typeof e == "string" || typeof e == "function" || e === X || e === U || te || e === B || e === $ || e === x || Y || e === ye || fe || Ee || p || typeof e == "object" && e !== null && (e.$$typeof === ue || e.$$typeof === I || e.$$typeof === F || e.$$typeof === G || e.$$typeof === R || // This needs to include all possible module reference object
        // types supported by any Flight configuration anywhere since
        // we don't know which Flight build this will end up being used
        // with.
        e.$$typeof === t || e.getModuleId !== void 0));
      }
      function c(e, r) {
        i(e) || h("memo: The first argument must be a component. Instead received: %s", e === null ? "null" : typeof e);
        var a = {
          $$typeof: I,
          type: e,
          compare: r === void 0 ? null : r
        };
        {
          var o;
          Object.defineProperty(a, "displayName", {
            enumerable: !1,
            configurable: !0,
            get: function() {
              return o;
            },
            set: function(s) {
              o = s, !e.name && !e.displayName && (e.displayName = s);
            }
          });
        }
        return a;
      }
      function f() {
        var e = ee.current;
        return e === null && h(`Invalid hook call. Hooks can only be called inside of the body of a function component. This could happen for one of the following reasons:
1. You might have mismatching versions of React and the renderer (such as React DOM)
2. You might be breaking the Rules of Hooks
3. You might have more than one copy of React in the same app
See https://reactjs.org/link/invalid-hook-call for tips about how to debug and fix this problem.`), e;
      }
      function j(e) {
        var r = f();
        if (e._context !== void 0) {
          var a = e._context;
          a.Consumer === e ? h("Calling useContext(Context.Consumer) is not supported, may cause bugs, and will be removed in a future major release. Did you mean to call useContext(Context) instead?") : a.Provider === e && h("Calling useContext(Context.Provider) is not supported. Did you mean to call useContext(Context) instead?");
        }
        return r.useContext(e);
      }
      function D(e) {
        var r = f();
        return r.useState(e);
      }
      function w(e, r, a) {
        var o = f();
        return o.useReducer(e, r, a);
      }
      function _(e) {
        var r = f();
        return r.useRef(e);
      }
      function pe(e, r) {
        var a = f();
        return a.useEffect(e, r);
      }
      function Z(e, r) {
        var a = f();
        return a.useInsertionEffect(e, r);
      }
      function re(e, r) {
        var a = f();
        return a.useLayoutEffect(e, r);
      }
      function me(e, r) {
        var a = f();
        return a.useCallback(e, r);
      }
      function Oe(e, r) {
        var a = f();
        return a.useMemo(e, r);
      }
      function Ce(e, r, a) {
        var o = f();
        return o.useImperativeHandle(e, r, a);
      }
      function he(e, r) {
        {
          var a = f();
          return a.useDebugValue(e, r);
        }
      }
      function Ze() {
        var e = f();
        return e.useTransition();
      }
      function Fr(e) {
        var r = f();
        return r.useDeferredValue(e);
      }
      function xr() {
        var e = f();
        return e.useId();
      }
      function Et(e, r, a) {
        var o = f();
        return o.useSyncExternalStore(e, r, a);
      }
      var Qe = 0, Yr, Br, Gr, Kr, Hr, qr, Jr;
      function Zr() {
      }
      Zr.__reactDisabledLog = !0;
      function _t() {
        {
          if (Qe === 0) {
            Yr = console.log, Br = console.info, Gr = console.warn, Kr = console.error, Hr = console.group, qr = console.groupCollapsed, Jr = console.groupEnd;
            var e = {
              configurable: !0,
              enumerable: !0,
              value: Zr,
              writable: !0
            };
            Object.defineProperties(console, {
              info: e,
              log: e,
              warn: e,
              error: e,
              group: e,
              groupCollapsed: e,
              groupEnd: e
            });
          }
          Qe++;
        }
      }
      function bt() {
        {
          if (Qe--, Qe === 0) {
            var e = {
              configurable: !0,
              enumerable: !0,
              writable: !0
            };
            Object.defineProperties(console, {
              log: u({}, e, {
                value: Yr
              }),
              info: u({}, e, {
                value: Br
              }),
              warn: u({}, e, {
                value: Gr
              }),
              error: u({}, e, {
                value: Kr
              }),
              group: u({}, e, {
                value: Hr
              }),
              groupCollapsed: u({}, e, {
                value: qr
              }),
              groupEnd: u({}, e, {
                value: Jr
              })
            });
          }
          Qe < 0 && h("disabledDepth fell below zero. This is a bug in React. Please file an issue.");
        }
      }
      var Nr = de.ReactCurrentDispatcher, Dr;
      function dr(e, r, a) {
        {
          if (Dr === void 0)
            try {
              throw Error();
            } catch (s) {
              var o = s.stack.trim().match(/\n( *(at )?)/);
              Dr = o && o[1] || "";
            }
          return `
` + Dr + e;
        }
      }
      var Ur = !1, pr;
      {
        var Rt = typeof WeakMap == "function" ? WeakMap : Map;
        pr = new Rt();
      }
      function Qr(e, r) {
        if (!e || Ur)
          return "";
        {
          var a = pr.get(e);
          if (a !== void 0)
            return a;
        }
        var o;
        Ur = !0;
        var s = Error.prepareStackTrace;
        Error.prepareStackTrace = void 0;
        var y;
        y = Nr.current, Nr.current = null, _t();
        try {
          if (r) {
            var d = function() {
              throw Error();
            };
            if (Object.defineProperty(d.prototype, "props", {
              set: function() {
                throw Error();
              }
            }), typeof Reflect == "object" && Reflect.construct) {
              try {
                Reflect.construct(d, []);
              } catch (ve) {
                o = ve;
              }
              Reflect.construct(e, [], d);
            } else {
              try {
                d.call();
              } catch (ve) {
                o = ve;
              }
              e.call(d.prototype);
            }
          } else {
            try {
              throw Error();
            } catch (ve) {
              o = ve;
            }
            e();
          }
        } catch (ve) {
          if (ve && o && typeof ve.stack == "string") {
            for (var m = ve.stack.split(`
`), O = o.stack.split(`
`), V = m.length - 1, K = O.length - 1; V >= 1 && K >= 0 && m[V] !== O[K]; )
              K--;
            for (; V >= 1 && K >= 0; V--, K--)
              if (m[V] !== O[K]) {
                if (V !== 1 || K !== 1)
                  do
                    if (V--, K--, K < 0 || m[V] !== O[K]) {
                      var H = `
` + m[V].replace(" at new ", " at ");
                      return e.displayName && H.includes("<anonymous>") && (H = H.replace("<anonymous>", e.displayName)), typeof e == "function" && pr.set(e, H), H;
                    }
                  while (V >= 1 && K >= 0);
                break;
              }
          }
        } finally {
          Ur = !1, Nr.current = y, bt(), Error.prepareStackTrace = s;
        }
        var Q = e ? e.displayName || e.name : "", oe = Q ? dr(Q) : "";
        return typeof e == "function" && pr.set(e, oe), oe;
      }
      function St(e, r, a) {
        return Qr(e, !1);
      }
      function Ct(e) {
        var r = e.prototype;
        return !!(r && r.isReactComponent);
      }
      function vr(e, r, a) {
        if (e == null)
          return "";
        if (typeof e == "function")
          return Qr(e, Ct(e));
        if (typeof e == "string")
          return dr(e);
        switch (e) {
          case $:
            return dr("Suspense");
          case x:
            return dr("SuspenseList");
        }
        if (typeof e == "object")
          switch (e.$$typeof) {
            case R:
              return St(e.render);
            case I:
              return vr(e.type, r, a);
            case ue: {
              var o = e, s = o._payload, y = o._init;
              try {
                return vr(y(s), r, a);
              } catch {
              }
            }
          }
        return "";
      }
      var Xr = {}, et = de.ReactDebugCurrentFrame;
      function hr(e) {
        if (e) {
          var r = e._owner, a = vr(e.type, e._source, r ? r.type : null);
          et.setExtraStackFrame(a);
        } else
          et.setExtraStackFrame(null);
      }
      function wt(e, r, a, o, s) {
        {
          var y = Function.call.bind(Fe);
          for (var d in e)
            if (y(e, d)) {
              var m = void 0;
              try {
                if (typeof e[d] != "function") {
                  var O = Error((o || "React class") + ": " + a + " type `" + d + "` is invalid; it must be a function, usually from the `prop-types` package, but received `" + typeof e[d] + "`.This often happens because of typos such as `PropTypes.function` instead of `PropTypes.func`.");
                  throw O.name = "Invariant Violation", O;
                }
                m = e[d](r, d, o, a, null, "SECRET_DO_NOT_PASS_THIS_OR_YOU_WILL_BE_FIRED");
              } catch (V) {
                m = V;
              }
              m && !(m instanceof Error) && (hr(s), h("%s: type specification of %s `%s` is invalid; the type checker function must return `null` or an `Error` but returned a %s. You may have forgotten to pass an argument to the type checker creator (arrayOf, instanceOf, objectOf, oneOf, oneOfType, and shape all require an argument).", o || "React class", a, d, typeof m), hr(null)), m instanceof Error && !(m.message in Xr) && (Xr[m.message] = !0, hr(s), h("Failed %s type: %s", a, m.message), hr(null));
            }
        }
      }
      function ze(e) {
        if (e) {
          var r = e._owner, a = vr(e.type, e._source, r ? r.type : null);
          ce(a);
        } else
          ce(null);
      }
      var $r;
      $r = !1;
      function rt() {
        if (S.current) {
          var e = Re(S.current.type);
          if (e)
            return `

Check the render method of \`` + e + "`.";
        }
        return "";
      }
      function At(e) {
        if (e !== void 0) {
          var r = e.fileName.replace(/^.*[\\\/]/, ""), a = e.lineNumber;
          return `

Check your code at ` + r + ":" + a + ".";
        }
        return "";
      }
      function Tt(e) {
        return e != null ? At(e.__source) : "";
      }
      var tt = {};
      function Ot(e) {
        var r = rt();
        if (!r) {
          var a = typeof e == "string" ? e : e.displayName || e.name;
          a && (r = `

Check the top-level render call using <` + a + ">.");
        }
        return r;
      }
      function nt(e, r) {
        if (!(!e._store || e._store.validated || e.key != null)) {
          e._store.validated = !0;
          var a = Ot(r);
          if (!tt[a]) {
            tt[a] = !0;
            var o = "";
            e && e._owner && e._owner !== S.current && (o = " It was passed a child from " + Re(e._owner.type) + "."), ze(e), h('Each child in a list should have a unique "key" prop.%s%s See https://reactjs.org/link/warning-keys for more information.', a, o), ze(null);
          }
        }
      }
      function at(e, r) {
        if (typeof e == "object") {
          if (Ue(e))
            for (var a = 0; a < e.length; a++) {
              var o = e[a];
              Ae(o) && nt(o, r);
            }
          else if (Ae(e))
            e._store && (e._store.validated = !0);
          else if (e) {
            var s = E(e);
            if (typeof s == "function" && s !== e.entries)
              for (var y = s.call(e), d; !(d = y.next()).done; )
                Ae(d.value) && nt(d.value, r);
          }
        }
      }
      function ot(e) {
        {
          var r = e.type;
          if (r == null || typeof r == "string")
            return;
          var a;
          if (typeof r == "function")
            a = r.propTypes;
          else if (typeof r == "object" && (r.$$typeof === R || // Note: Memo only checks outer props here.
          // Inner props are checked in the reconciler.
          r.$$typeof === I))
            a = r.propTypes;
          else
            return;
          if (a) {
            var o = Re(r);
            wt(a, e.props, "prop", o, e);
          } else if (r.PropTypes !== void 0 && !$r) {
            $r = !0;
            var s = Re(r);
            h("Component %s declared `PropTypes` instead of `propTypes`. Did you misspell the property assignment?", s || "Unknown");
          }
          typeof r.getDefaultProps == "function" && !r.getDefaultProps.isReactClassApproved && h("getDefaultProps is only used on classic React.createClass definitions. Use a static property named `defaultProps` instead.");
        }
      }
      function Pt(e) {
        {
          for (var r = Object.keys(e.props), a = 0; a < r.length; a++) {
            var o = r[a];
            if (o !== "children" && o !== "key") {
              ze(e), h("Invalid prop `%s` supplied to `React.Fragment`. React.Fragment can only have `key` and `children` props.", o), ze(null);
              break;
            }
          }
          e.ref !== null && (ze(e), h("Invalid attribute `ref` supplied to `React.Fragment`."), ze(null));
        }
      }
      function it(e, r, a) {
        var o = i(e);
        if (!o) {
          var s = "";
          (e === void 0 || typeof e == "object" && e !== null && Object.keys(e).length === 0) && (s += " You likely forgot to export your component from the file it's defined in, or you might have mixed up default and named imports.");
          var y = Tt(r);
          y ? s += y : s += rt();
          var d;
          e === null ? d = "null" : Ue(e) ? d = "array" : e !== void 0 && e.$$typeof === q ? (d = "<" + (Re(e.type) || "Unknown") + " />", s = " Did you accidentally export a JSX literal instead of a component?") : d = typeof e, h("React.createElement: type is invalid -- expected a string (for built-in components) or a class/function (for composite components) but got: %s.%s", d, s);
        }
        var m = Rr.apply(this, arguments);
        if (m == null)
          return m;
        if (o)
          for (var O = 2; O < arguments.length; O++)
            at(arguments[O], e);
        return e === X ? Pt(m) : ot(m), m;
      }
      var ut = !1;
      function jt(e) {
        var r = it.bind(null, e);
        return r.type = e, ut || (ut = !0, _e("React.createFactory() is deprecated and will be removed in a future major release. Consider using JSX or use React.createElement() directly instead.")), Object.defineProperty(r, "type", {
          enumerable: !1,
          get: function() {
            return _e("Factory.type is deprecated. Access the class directly before passing it to createFactory."), Object.defineProperty(this, "type", {
              value: e
            }), e;
          }
        }), r;
      }
      function kt(e, r, a) {
        for (var o = Cr.apply(this, arguments), s = 2; s < arguments.length; s++)
          at(arguments[s], o.type);
        return ot(o), o;
      }
      function Ft(e, r) {
        var a = g.transition;
        g.transition = {};
        var o = g.transition;
        g.transition._updatedFibers = /* @__PURE__ */ new Set();
        try {
          e();
        } finally {
          if (g.transition = a, a === null && o._updatedFibers) {
            var s = o._updatedFibers.size;
            s > 10 && _e("Detected a large number of updates inside startTransition. If this is due to a subscription please re-write it to use React provided hooks. Otherwise concurrent mode guarantees are off the table."), o._updatedFibers.clear();
          }
        }
      }
      var st = !1, yr = null;
      function xt(e) {
        if (yr === null)
          try {
            var r = ("require" + Math.random()).slice(0, 7), a = k && k[r];
            yr = a.call(k, "timers").setImmediate;
          } catch {
            yr = function(s) {
              st === !1 && (st = !0, typeof MessageChannel > "u" && h("This browser does not have a MessageChannel implementation, so enqueuing tasks via await act(async () => ...) will fail. Please file an issue at https://github.com/facebook/react/issues if you encounter this warning."));
              var y = new MessageChannel();
              y.port1.onmessage = s, y.port2.postMessage(void 0);
            };
          }
        return yr(e);
      }
      var Ye = 0, ct = !1;
      function lt(e) {
        {
          var r = Ye;
          Ye++, v.current === null && (v.current = []);
          var a = v.isBatchingLegacy, o;
          try {
            if (v.isBatchingLegacy = !0, o = e(), !a && v.didScheduleLegacyUpdate) {
              var s = v.current;
              s !== null && (v.didScheduleLegacyUpdate = !1, Mr(s));
            }
          } catch (Q) {
            throw mr(r), Q;
          } finally {
            v.isBatchingLegacy = a;
          }
          if (o !== null && typeof o == "object" && typeof o.then == "function") {
            var y = o, d = !1, m = {
              then: function(Q, oe) {
                d = !0, y.then(function(ve) {
                  mr(r), Ye === 0 ? Ir(ve, Q, oe) : Q(ve);
                }, function(ve) {
                  mr(r), oe(ve);
                });
              }
            };
            return !ct && typeof Promise < "u" && Promise.resolve().then(function() {
            }).then(function() {
              d || (ct = !0, h("You called act(async () => ...) without await. This could lead to unexpected testing behaviour, interleaving multiple act calls and mixing their scopes. You should - await act(async () => ...);"));
            }), m;
          } else {
            var O = o;
            if (mr(r), Ye === 0) {
              var V = v.current;
              V !== null && (Mr(V), v.current = null);
              var K = {
                then: function(Q, oe) {
                  v.current === null ? (v.current = [], Ir(O, Q, oe)) : Q(O);
                }
              };
              return K;
            } else {
              var H = {
                then: function(Q, oe) {
                  Q(O);
                }
              };
              return H;
            }
          }
        }
      }
      function mr(e) {
        e !== Ye - 1 && h("You seem to have overlapping act() calls, this is not supported. Be sure to await previous act() calls before making a new one. "), Ye = e;
      }
      function Ir(e, r, a) {
        {
          var o = v.current;
          if (o !== null)
            try {
              Mr(o), xt(function() {
                o.length === 0 ? (v.current = null, r(e)) : Ir(e, r, a);
              });
            } catch (s) {
              a(s);
            }
          else
            r(e);
        }
      }
      var Lr = !1;
      function Mr(e) {
        if (!Lr) {
          Lr = !0;
          var r = 0;
          try {
            for (; r < e.length; r++) {
              var a = e[r];
              do
                a = a(!0);
              while (a !== null);
            }
            e.length = 0;
          } catch (o) {
            throw e = e.slice(r + 1), o;
          } finally {
            Lr = !1;
          }
        }
      }
      var Nt = it, Dt = kt, Ut = jt, $t = {
        map: We,
        forEach: Ar,
        count: cr,
        toArray: lr,
        only: fr
      };
      l.Children = $t, l.Component = A, l.Fragment = X, l.Profiler = U, l.PureComponent = N, l.StrictMode = B, l.Suspense = $, l.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED = de, l.act = lt, l.cloneElement = Dt, l.createContext = Tr, l.createElement = Nt, l.createFactory = Ut, l.createRef = Er, l.forwardRef = kr, l.isValidElement = Ae, l.lazy = jr, l.memo = c, l.startTransition = Ft, l.unstable_act = lt, l.useCallback = me, l.useContext = j, l.useDebugValue = he, l.useDeferredValue = Fr, l.useEffect = pe, l.useId = xr, l.useImperativeHandle = Ce, l.useInsertionEffect = Z, l.useLayoutEffect = re, l.useMemo = Oe, l.useReducer = w, l.useRef = _, l.useState = D, l.useSyncExternalStore = Et, l.useTransition = Ze, l.version = M, typeof __REACT_DEVTOOLS_GLOBAL_HOOK__ < "u" && typeof __REACT_DEVTOOLS_GLOBAL_HOOK__.registerInternalModuleStop == "function" && __REACT_DEVTOOLS_GLOBAL_HOOK__.registerInternalModuleStop(new Error());
    }();
  }(rr, rr.exports)), rr.exports;
}
process.env.NODE_ENV === "production" ? zr.exports = Wt() : zr.exports = Vt();
var ge = zr.exports;
/**
 * @license React
 * react-jsx-runtime.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
var yt;
function zt() {
  if (yt) return Xe;
  yt = 1;
  var k = ge, l = Symbol.for("react.element"), M = Symbol.for("react.fragment"), q = Object.prototype.hasOwnProperty, ie = k.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner, X = { key: !0, ref: !0, __self: !0, __source: !0 };
  function B(U, F, G) {
    var R, $ = {}, x = null, I = null;
    G !== void 0 && (x = "" + G), F.key !== void 0 && (x = "" + F.key), F.ref !== void 0 && (I = F.ref);
    for (R in F) q.call(F, R) && !X.hasOwnProperty(R) && ($[R] = F[R]);
    if (U && U.defaultProps) for (R in F = U.defaultProps, F) $[R] === void 0 && ($[R] = F[R]);
    return { $$typeof: l, type: U, key: x, ref: I, props: $, _owner: ie.current };
  }
  return Xe.Fragment = M, Xe.jsx = B, Xe.jsxs = B, Xe;
}
var er = {};
/**
 * @license React
 * react-jsx-runtime.development.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
var mt;
function Yt() {
  return mt || (mt = 1, process.env.NODE_ENV !== "production" && function() {
    var k = ge, l = Symbol.for("react.element"), M = Symbol.for("react.portal"), q = Symbol.for("react.fragment"), ie = Symbol.for("react.strict_mode"), X = Symbol.for("react.profiler"), B = Symbol.for("react.provider"), U = Symbol.for("react.context"), F = Symbol.for("react.forward_ref"), G = Symbol.for("react.suspense"), R = Symbol.for("react.suspense_list"), $ = Symbol.for("react.memo"), x = Symbol.for("react.lazy"), I = Symbol.for("react.offscreen"), ue = Symbol.iterator, ye = "@@iterator";
    function ne(t) {
      if (t === null || typeof t != "object")
        return null;
      var i = ue && t[ue] || t[ye];
      return typeof i == "function" ? i : null;
    }
    var le = k.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED;
    function E(t) {
      {
        for (var i = arguments.length, c = new Array(i > 1 ? i - 1 : 0), f = 1; f < i; f++)
          c[f - 1] = arguments[f];
        ee("error", t, c);
      }
    }
    function ee(t, i, c) {
      {
        var f = le.ReactDebugCurrentFrame, j = f.getStackAddendum();
        j !== "" && (i += "%s", c = c.concat([j]));
        var D = c.map(function(w) {
          return String(w);
        });
        D.unshift("Warning: " + i), Function.prototype.apply.call(console[t], console, D);
      }
    }
    var g = !1, v = !1, S = !1, J = !1, se = !1, ce;
    ce = Symbol.for("react.module.reference");
    function fe(t) {
      return !!(typeof t == "string" || typeof t == "function" || t === q || t === X || se || t === ie || t === G || t === R || J || t === I || g || v || S || typeof t == "object" && t !== null && (t.$$typeof === x || t.$$typeof === $ || t.$$typeof === B || t.$$typeof === U || t.$$typeof === F || // This needs to include all possible module reference object
      // types supported by any Flight configuration anywhere since
      // we don't know which Flight build this will end up being used
      // with.
      t.$$typeof === ce || t.getModuleId !== void 0));
    }
    function Ee(t, i, c) {
      var f = t.displayName;
      if (f)
        return f;
      var j = i.displayName || i.name || "";
      return j !== "" ? c + "(" + j + ")" : c;
    }
    function p(t) {
      return t.displayName || "Context";
    }
    function Y(t) {
      if (t == null)
        return null;
      if (typeof t.tag == "number" && E("Received an unexpected object in getComponentNameFromType(). This is likely a bug in React. Please file an issue."), typeof t == "function")
        return t.displayName || t.name || null;
      if (typeof t == "string")
        return t;
      switch (t) {
        case q:
          return "Fragment";
        case M:
          return "Portal";
        case X:
          return "Profiler";
        case ie:
          return "StrictMode";
        case G:
          return "Suspense";
        case R:
          return "SuspenseList";
      }
      if (typeof t == "object")
        switch (t.$$typeof) {
          case U:
            var i = t;
            return p(i) + ".Consumer";
          case B:
            var c = t;
            return p(c._context) + ".Provider";
          case F:
            return Ee(t, t.render, "ForwardRef");
          case $:
            var f = t.displayName || null;
            return f !== null ? f : Y(t.type) || "Memo";
          case x: {
            var j = t, D = j._payload, w = j._init;
            try {
              return Y(w(D));
            } catch {
              return null;
            }
          }
        }
      return null;
    }
    var te = Object.assign, de = 0, _e, h, be, Pe, Se, n, u;
    function C() {
    }
    C.__reactDisabledLog = !0;
    function A() {
      {
        if (de === 0) {
          _e = console.log, h = console.info, be = console.warn, Pe = console.error, Se = console.group, n = console.groupCollapsed, u = console.groupEnd;
          var t = {
            configurable: !0,
            enumerable: !0,
            value: C,
            writable: !0
          };
          Object.defineProperties(console, {
            info: t,
            log: t,
            warn: t,
            error: t,
            group: t,
            groupCollapsed: t,
            groupEnd: t
          });
        }
        de++;
      }
    }
    function P() {
      {
        if (de--, de === 0) {
          var t = {
            configurable: !0,
            enumerable: !0,
            writable: !0
          };
          Object.defineProperties(console, {
            log: te({}, t, {
              value: _e
            }),
            info: te({}, t, {
              value: h
            }),
            warn: te({}, t, {
              value: be
            }),
            error: te({}, t, {
              value: Pe
            }),
            group: te({}, t, {
              value: Se
            }),
            groupCollapsed: te({}, t, {
              value: n
            }),
            groupEnd: te({}, t, {
              value: u
            })
          });
        }
        de < 0 && E("disabledDepth fell below zero. This is a bug in React. Please file an issue.");
      }
    }
    var z = le.ReactCurrentDispatcher, W;
    function L(t, i, c) {
      {
        if (W === void 0)
          try {
            throw Error();
          } catch (j) {
            var f = j.stack.trim().match(/\n( *(at )?)/);
            W = f && f[1] || "";
          }
        return `
` + W + t;
      }
    }
    var N = !1, ae;
    {
      var Er = typeof WeakMap == "function" ? WeakMap : Map;
      ae = new Er();
    }
    function tr(t, i) {
      if (!t || N)
        return "";
      {
        var c = ae.get(t);
        if (c !== void 0)
          return c;
      }
      var f;
      N = !0;
      var j = Error.prepareStackTrace;
      Error.prepareStackTrace = void 0;
      var D;
      D = z.current, z.current = null, A();
      try {
        if (i) {
          var w = function() {
            throw Error();
          };
          if (Object.defineProperty(w.prototype, "props", {
            set: function() {
              throw Error();
            }
          }), typeof Reflect == "object" && Reflect.construct) {
            try {
              Reflect.construct(w, []);
            } catch (he) {
              f = he;
            }
            Reflect.construct(t, [], w);
          } else {
            try {
              w.call();
            } catch (he) {
              f = he;
            }
            t.call(w.prototype);
          }
        } else {
          try {
            throw Error();
          } catch (he) {
            f = he;
          }
          t();
        }
      } catch (he) {
        if (he && f && typeof he.stack == "string") {
          for (var _ = he.stack.split(`
`), pe = f.stack.split(`
`), Z = _.length - 1, re = pe.length - 1; Z >= 1 && re >= 0 && _[Z] !== pe[re]; )
            re--;
          for (; Z >= 1 && re >= 0; Z--, re--)
            if (_[Z] !== pe[re]) {
              if (Z !== 1 || re !== 1)
                do
                  if (Z--, re--, re < 0 || _[Z] !== pe[re]) {
                    var me = `
` + _[Z].replace(" at new ", " at ");
                    return t.displayName && me.includes("<anonymous>") && (me = me.replace("<anonymous>", t.displayName)), typeof t == "function" && ae.set(t, me), me;
                  }
                while (Z >= 1 && re >= 0);
              break;
            }
        }
      } finally {
        N = !1, z.current = D, P(), Error.prepareStackTrace = j;
      }
      var Oe = t ? t.displayName || t.name : "", Ce = Oe ? L(Oe) : "";
      return typeof t == "function" && ae.set(t, Ce), Ce;
    }
    function Ue(t, i, c) {
      return tr(t, !1);
    }
    function _r(t) {
      var i = t.prototype;
      return !!(i && i.isReactComponent);
    }
    function $e(t, i, c) {
      if (t == null)
        return "";
      if (typeof t == "function")
        return tr(t, _r(t));
      if (typeof t == "string")
        return L(t);
      switch (t) {
        case G:
          return L("Suspense");
        case R:
          return L("SuspenseList");
      }
      if (typeof t == "object")
        switch (t.$$typeof) {
          case F:
            return Ue(t.render);
          case $:
            return $e(t.type, i, c);
          case x: {
            var f = t, j = f._payload, D = f._init;
            try {
              return $e(D(j), i, c);
            } catch {
            }
          }
        }
      return "";
    }
    var we = Object.prototype.hasOwnProperty, je = {}, nr = le.ReactDebugCurrentFrame;
    function ke(t) {
      if (t) {
        var i = t._owner, c = $e(t.type, t._source, i ? i.type : null);
        nr.setExtraStackFrame(c);
      } else
        nr.setExtraStackFrame(null);
    }
    function Re(t, i, c, f, j) {
      {
        var D = Function.call.bind(we);
        for (var w in t)
          if (D(t, w)) {
            var _ = void 0;
            try {
              if (typeof t[w] != "function") {
                var pe = Error((f || "React class") + ": " + c + " type `" + w + "` is invalid; it must be a function, usually from the `prop-types` package, but received `" + typeof t[w] + "`.This often happens because of typos such as `PropTypes.function` instead of `PropTypes.func`.");
                throw pe.name = "Invariant Violation", pe;
              }
              _ = t[w](i, w, f, c, null, "SECRET_DO_NOT_PASS_THIS_OR_YOU_WILL_BE_FIRED");
            } catch (Z) {
              _ = Z;
            }
            _ && !(_ instanceof Error) && (ke(j), E("%s: type specification of %s `%s` is invalid; the type checker function must return `null` or an `Error` but returned a %s. You may have forgotten to pass an argument to the type checker creator (arrayOf, instanceOf, objectOf, oneOf, oneOfType, and shape all require an argument).", f || "React class", c, w, typeof _), ke(null)), _ instanceof Error && !(_.message in je) && (je[_.message] = !0, ke(j), E("Failed %s type: %s", c, _.message), ke(null));
          }
      }
    }
    var Fe = Array.isArray;
    function Ie(t) {
      return Fe(t);
    }
    function ar(t) {
      {
        var i = typeof Symbol == "function" && Symbol.toStringTag, c = i && t[Symbol.toStringTag] || t.constructor.name || "Object";
        return c;
      }
    }
    function or(t) {
      try {
        return Le(t), !1;
      } catch {
        return !0;
      }
    }
    function Le(t) {
      return "" + t;
    }
    function Be(t) {
      if (or(t))
        return E("The provided key is an unsupported type %s. This value must be coerced to a string before before using it here.", ar(t)), Le(t);
    }
    var Ge = le.ReactCurrentOwner, br = {
      key: !0,
      ref: !0,
      __self: !0,
      __source: !0
    }, ir, ur;
    function Ke(t) {
      if (we.call(t, "ref")) {
        var i = Object.getOwnPropertyDescriptor(t, "ref").get;
        if (i && i.isReactWarning)
          return !1;
      }
      return t.ref !== void 0;
    }
    function Rr(t) {
      if (we.call(t, "key")) {
        var i = Object.getOwnPropertyDescriptor(t, "key").get;
        if (i && i.isReactWarning)
          return !1;
      }
      return t.key !== void 0;
    }
    function Sr(t, i) {
      typeof t.ref == "string" && Ge.current;
    }
    function Cr(t, i) {
      {
        var c = function() {
          ir || (ir = !0, E("%s: `key` is not a prop. Trying to access it will result in `undefined` being returned. If you need to access the same value within the child component, you should pass it as a different prop. (https://reactjs.org/link/special-props)", i));
        };
        c.isReactWarning = !0, Object.defineProperty(t, "key", {
          get: c,
          configurable: !0
        });
      }
    }
    function Ae(t, i) {
      {
        var c = function() {
          ur || (ur = !0, E("%s: `ref` is not a prop. Trying to access it will result in `undefined` being returned. If you need to access the same value within the child component, you should pass it as a different prop. (https://reactjs.org/link/special-props)", i));
        };
        c.isReactWarning = !0, Object.defineProperty(t, "ref", {
          get: c,
          configurable: !0
        });
      }
    }
    var sr = function(t, i, c, f, j, D, w) {
      var _ = {
        // This tag allows us to uniquely identify this as a React Element
        $$typeof: l,
        // Built-in properties that belong on the element
        type: t,
        key: i,
        ref: c,
        props: w,
        // Record the component responsible for creating this element.
        _owner: D
      };
      return _._store = {}, Object.defineProperty(_._store, "validated", {
        configurable: !1,
        enumerable: !1,
        writable: !0,
        value: !1
      }), Object.defineProperty(_, "_self", {
        configurable: !1,
        enumerable: !1,
        writable: !1,
        value: f
      }), Object.defineProperty(_, "_source", {
        configurable: !1,
        enumerable: !1,
        writable: !1,
        value: j
      }), Object.freeze && (Object.freeze(_.props), Object.freeze(_)), _;
    };
    function wr(t, i, c, f, j) {
      {
        var D, w = {}, _ = null, pe = null;
        c !== void 0 && (Be(c), _ = "" + c), Rr(i) && (Be(i.key), _ = "" + i.key), Ke(i) && (pe = i.ref, Sr(i, j));
        for (D in i)
          we.call(i, D) && !br.hasOwnProperty(D) && (w[D] = i[D]);
        if (t && t.defaultProps) {
          var Z = t.defaultProps;
          for (D in Z)
            w[D] === void 0 && (w[D] = Z[D]);
        }
        if (_ || pe) {
          var re = typeof t == "function" ? t.displayName || t.name || "Unknown" : t;
          _ && Cr(w, re), pe && Ae(w, re);
        }
        return sr(t, _, pe, j, f, Ge.current, w);
      }
    }
    var He = le.ReactCurrentOwner, qe = le.ReactDebugCurrentFrame;
    function Te(t) {
      if (t) {
        var i = t._owner, c = $e(t.type, t._source, i ? i.type : null);
        qe.setExtraStackFrame(c);
      } else
        qe.setExtraStackFrame(null);
    }
    var Me;
    Me = !1;
    function xe(t) {
      return typeof t == "object" && t !== null && t.$$typeof === l;
    }
    function Ne() {
      {
        if (He.current) {
          var t = Y(He.current.type);
          if (t)
            return `

Check the render method of \`` + t + "`.";
        }
        return "";
      }
    }
    function We(t) {
      return "";
    }
    var cr = {};
    function Ar(t) {
      {
        var i = Ne();
        if (!i) {
          var c = typeof t == "string" ? t : t.displayName || t.name;
          c && (i = `

Check the top-level render call using <` + c + ">.");
        }
        return i;
      }
    }
    function lr(t, i) {
      {
        if (!t._store || t._store.validated || t.key != null)
          return;
        t._store.validated = !0;
        var c = Ar(i);
        if (cr[c])
          return;
        cr[c] = !0;
        var f = "";
        t && t._owner && t._owner !== He.current && (f = " It was passed a child from " + Y(t._owner.type) + "."), Te(t), E('Each child in a list should have a unique "key" prop.%s%s See https://reactjs.org/link/warning-keys for more information.', c, f), Te(null);
      }
    }
    function fr(t, i) {
      {
        if (typeof t != "object")
          return;
        if (Ie(t))
          for (var c = 0; c < t.length; c++) {
            var f = t[c];
            xe(f) && lr(f, i);
          }
        else if (xe(t))
          t._store && (t._store.validated = !0);
        else if (t) {
          var j = ne(t);
          if (typeof j == "function" && j !== t.entries)
            for (var D = j.call(t), w; !(w = D.next()).done; )
              xe(w.value) && lr(w.value, i);
        }
      }
    }
    function Tr(t) {
      {
        var i = t.type;
        if (i == null || typeof i == "string")
          return;
        var c;
        if (typeof i == "function")
          c = i.propTypes;
        else if (typeof i == "object" && (i.$$typeof === F || // Note: Memo only checks outer props here.
        // Inner props are checked in the reconciler.
        i.$$typeof === $))
          c = i.propTypes;
        else
          return;
        if (c) {
          var f = Y(i);
          Re(c, t.props, "prop", f, t);
        } else if (i.PropTypes !== void 0 && !Me) {
          Me = !0;
          var j = Y(i);
          E("Component %s declared `PropTypes` instead of `propTypes`. Did you misspell the property assignment?", j || "Unknown");
        }
        typeof i.getDefaultProps == "function" && !i.getDefaultProps.isReactClassApproved && E("getDefaultProps is only used on classic React.createClass definitions. Use a static property named `defaultProps` instead.");
      }
    }
    function De(t) {
      {
        for (var i = Object.keys(t.props), c = 0; c < i.length; c++) {
          var f = i[c];
          if (f !== "children" && f !== "key") {
            Te(t), E("Invalid prop `%s` supplied to `React.Fragment`. React.Fragment can only have `key` and `children` props.", f), Te(null);
            break;
          }
        }
        t.ref !== null && (Te(t), E("Invalid attribute `ref` supplied to `React.Fragment`."), Te(null));
      }
    }
    var Ve = {};
    function Je(t, i, c, f, j, D) {
      {
        var w = fe(t);
        if (!w) {
          var _ = "";
          (t === void 0 || typeof t == "object" && t !== null && Object.keys(t).length === 0) && (_ += " You likely forgot to export your component from the file it's defined in, or you might have mixed up default and named imports.");
          var pe = We();
          pe ? _ += pe : _ += Ne();
          var Z;
          t === null ? Z = "null" : Ie(t) ? Z = "array" : t !== void 0 && t.$$typeof === l ? (Z = "<" + (Y(t.type) || "Unknown") + " />", _ = " Did you accidentally export a JSX literal instead of a component?") : Z = typeof t, E("React.jsx: type is invalid -- expected a string (for built-in components) or a class/function (for composite components) but got: %s.%s", Z, _);
        }
        var re = wr(t, i, c, j, D);
        if (re == null)
          return re;
        if (w) {
          var me = i.children;
          if (me !== void 0)
            if (f)
              if (Ie(me)) {
                for (var Oe = 0; Oe < me.length; Oe++)
                  fr(me[Oe], t);
                Object.freeze && Object.freeze(me);
              } else
                E("React.jsx: Static children should always be an array. You are likely explicitly calling React.jsxs or React.jsxDEV. Use the Babel transform instead.");
            else
              fr(me, t);
        }
        if (we.call(i, "key")) {
          var Ce = Y(t), he = Object.keys(i).filter(function(xr) {
            return xr !== "key";
          }), Ze = he.length > 0 ? "{key: someKey, " + he.join(": ..., ") + ": ...}" : "{key: someKey}";
          if (!Ve[Ce + Ze]) {
            var Fr = he.length > 0 ? "{" + he.join(": ..., ") + ": ...}" : "{}";
            E(`A props object containing a "key" prop is being spread into JSX:
  let props = %s;
  <%s {...props} />
React keys must be passed directly to JSX without using spread:
  let props = %s;
  <%s key={someKey} {...props} />`, Ze, Ce, Fr, Ce), Ve[Ce + Ze] = !0;
          }
        }
        return t === q ? De(re) : Tr(re), re;
      }
    }
    function Or(t, i, c) {
      return Je(t, i, c, !0);
    }
    function Pr(t, i, c) {
      return Je(t, i, c, !1);
    }
    var jr = Pr, kr = Or;
    er.Fragment = q, er.jsx = jr, er.jsxs = kr;
  }()), er;
}
process.env.NODE_ENV === "production" ? Vr.exports = zt() : Vr.exports = Yt();
var T = Vr.exports;
function Bt(k) {
  const l = {
    // Europe (EU)
    IT: "EU",
    GB: "EU",
    FR: "EU",
    DE: "EU",
    ES: "EU",
    PT: "EU",
    NL: "EU",
    BE: "EU",
    CH: "EU",
    AT: "EU",
    SE: "EU",
    NO: "EU",
    DK: "EU",
    FI: "EU",
    PL: "EU",
    GR: "EU",
    IE: "EU",
    CZ: "EU",
    HU: "EU",
    RO: "EU",
    BG: "EU",
    HR: "EU",
    SK: "EU",
    SI: "EU",
    LT: "EU",
    LV: "EU",
    EE: "EU",
    LU: "EU",
    MT: "EU",
    CY: "EU",
    IS: "EU",
    LI: "EU",
    MC: "EU",
    SM: "EU",
    VA: "EU",
    AD: "EU",
    AL: "EU",
    BA: "EU",
    ME: "EU",
    MK: "EU",
    RS: "EU",
    XK: "EU",
    UA: "EU",
    BY: "EU",
    MD: "EU",
    RU: "EU",
    // North America (NA)
    US: "NA",
    CA: "NA",
    MX: "NA",
    GT: "NA",
    BZ: "NA",
    SV: "NA",
    HN: "NA",
    NI: "NA",
    CR: "NA",
    PA: "NA",
    CU: "NA",
    JM: "NA",
    HT: "NA",
    DO: "NA",
    BS: "NA",
    BB: "NA",
    TT: "NA",
    // South America (SA)
    BR: "SA",
    AR: "SA",
    CL: "SA",
    CO: "SA",
    PE: "SA",
    VE: "SA",
    EC: "SA",
    BO: "SA",
    PY: "SA",
    UY: "SA",
    GY: "SA",
    SR: "SA",
    GF: "SA",
    FK: "SA",
    // Asia (AS)
    CN: "AS",
    JP: "AS",
    IN: "AS",
    KR: "AS",
    KP: "AS",
    ID: "AS",
    TH: "AS",
    VN: "AS",
    PH: "AS",
    MY: "AS",
    SG: "AS",
    BD: "AS",
    PK: "AS",
    AF: "AS",
    IR: "AS",
    IQ: "AS",
    SA: "AS",
    AE: "AS",
    IL: "AS",
    TR: "AS",
    KZ: "AS",
    UZ: "AS",
    MM: "AS",
    LK: "AS",
    NP: "AS",
    KH: "AS",
    LA: "AS",
    MN: "AS",
    TW: "AS",
    HK: "AS",
    MO: "AS",
    // Africa (AF)
    ZA: "AF",
    EG: "AF",
    NG: "AF",
    KE: "AF",
    ET: "AF",
    GH: "AF",
    TZ: "AF",
    UG: "AF",
    DZ: "AF",
    MA: "AF",
    SD: "AF",
    AO: "AF",
    MZ: "AF",
    ZM: "AF",
    ZW: "AF",
    MW: "AF",
    MG: "AF",
    CM: "AF",
    CI: "AF",
    SN: "AF",
    ML: "AF",
    BF: "AF",
    NE: "AF",
    TD: "AF",
    LY: "AF",
    TN: "AF",
    RW: "AF",
    BJ: "AF",
    TG: "AF",
    GN: "AF",
    SL: "AF",
    LR: "AF",
    MR: "AF",
    GM: "AF",
    GW: "AF",
    CV: "AF",
    ST: "AF",
    GQ: "AF",
    GA: "AF",
    CG: "AF",
    CD: "AF",
    CF: "AF",
    SS: "AF",
    ER: "AF",
    DJ: "AF",
    SO: "AF",
    KM: "AF",
    MU: "AF",
    SC: "AF",
    BW: "AF",
    NA: "AF",
    LS: "AF",
    SZ: "AF",
    // Oceania (OC)
    AU: "OC",
    NZ: "OC",
    PG: "OC",
    FJ: "OC",
    NC: "OC",
    PF: "OC",
    SB: "OC",
    VU: "OC",
    WS: "OC",
    TO: "OC",
    KI: "OC",
    FM: "OC",
    MH: "OC",
    PW: "OC",
    TV: "OC",
    NR: "OC",
    // Antarctica (AN)
    AQ: "AN"
  }, M = k.toUpperCase();
  return l[M] || "EU";
}
const gt = ({
  data: k,
  dataUrl: l,
  country: M,
  language: q,
  username: ie,
  password: X
}) => {
  const [B, U] = ge.useState(k || null), [F, G] = ge.useState(!1), [R, $] = ge.useState(null);
  return ge.useEffect(() => {
    if (k) {
      U(k);
      return;
    }
    if (M) {
      $(null), U(null);
      const I = (l || "https://raw.githubusercontent.com/flashboss/cities-generator/master/_db").replace(/\.json$/, "").replace(/\/$/, ""), ue = (q || "it").toLowerCase(), ye = M.toUpperCase(), ne = Bt(ye), le = `${I}/${ne}/${ye}/${ue}.json`;
      (async () => {
        G(!0), $(null);
        try {
          const ee = {};
          if (ie && X) {
            const S = btoa(`${ie}:${X}`);
            ee.Authorization = `Basic ${S}`;
          }
          const g = await fetch(le, { headers: ee });
          if (!g.ok)
            throw new Error(`Failed to load data: ${g.status} ${g.statusText}`);
          const v = await g.json();
          U(v);
        } catch (ee) {
          const g = ee instanceof Error ? ee.message : "Failed to load country data";
          $(g), U(null);
        } finally {
          G(!1);
        }
      })();
    }
  }, [k, M, q, l, ie, X]), { nodes: B, loading: F, error: R };
}, Gt = (k, l) => {
  const [M, q] = ge.useState(""), [ie, X] = ge.useState([]), B = (U, F, G = []) => {
    if (!U || !F) return [];
    const R = [], $ = U.toLowerCase();
    for (const x of F) {
      const I = [...G, x];
      x.name.toLowerCase().includes($) && R.push({ node: x, path: I }), x.zones && x.zones.length > 0 && R.push(...B(U, x.zones, I));
    }
    return R;
  };
  return ge.useEffect(() => {
    if (k && M && l) {
      const U = B(M, l.zones);
      X(U);
    } else
      X([]), k || q("");
  }, [M, l, k]), { searchQuery: M, setSearchQuery: q, searchResults: ie };
}, Kt = ({
  data: k,
  onSelect: l,
  className: M = "",
  dataUrl: q,
  country: ie = "IT",
  language: X = "it",
  placeholder: B = "Select location...",
  username: U,
  password: F,
  enableSearch: G = !1,
  searchPlaceholder: R = "Search location...",
  popup: $ = !1
}) => {
  const { nodes: x, loading: I, error: ue } = gt({
    data: k,
    dataUrl: q,
    country: ie,
    language: X,
    username: U,
    password: F
  }), { searchQuery: ye, setSearchQuery: ne, searchResults: le } = Gt(G, x), [E, ee] = ge.useState([]), [g, v] = ge.useState(!1), S = ge.useRef(null), J = (p, Y) => {
    const te = E.slice(0, Y);
    te[Y] = p, ee(te), (!p.zones || p.zones.length === 0) && ($ && alert(`Selected: ${p.name} (ID: ${p.id})`), l && l(p), v(!1));
  }, se = () => {
    if (!x) return [];
    if (E.length === 0) return x.zones;
    let p = E[E.length - 1];
    return (p == null ? void 0 : p.zones) || [];
  }, ce = () => E.length === 0 ? Array.isArray(B) ? B[0] || "Select location..." : B || "Select location..." : E.map((p) => p.name).join(" > "), fe = (p) => {
    p.stopPropagation(), ee([]), l && l(null);
  };
  if (I && !x)
    return /* @__PURE__ */ T.jsx("div", { className: `cities-dropdown loading ${M}`, children: "Loading..." });
  if (ue && !x)
    return /* @__PURE__ */ T.jsxs("div", { className: `cities-dropdown error ${M}`, children: [
      "Error: ",
      ue
    ] });
  const Ee = se();
  return /* @__PURE__ */ T.jsxs("div", { className: `cities-dropdown ${M}`, ref: S, children: [
    /* @__PURE__ */ T.jsxs(
      "div",
      {
        className: "cities-dropdown-trigger",
        onClick: () => {
          const p = !g;
          v(p), p || ne("");
        },
        role: "button",
        tabIndex: 0,
        "aria-expanded": g,
        "aria-haspopup": "listbox",
        children: [
          /* @__PURE__ */ T.jsx("span", { className: "cities-dropdown-text", children: ce() }),
          E.length > 0 && /* @__PURE__ */ T.jsx(
            "button",
            {
              className: "cities-dropdown-clear",
              onClick: fe,
              "aria-label": "Clear selection",
              children: "×"
            }
          ),
          /* @__PURE__ */ T.jsx("span", { className: "cities-dropdown-arrow", children: g ? "▲" : "▼" })
        ]
      }
    ),
    g && /* @__PURE__ */ T.jsxs("div", { className: "cities-dropdown-menu", role: "listbox", children: [
      G && /* @__PURE__ */ T.jsx("div", { className: "cities-dropdown-search", children: /* @__PURE__ */ T.jsx(
        "input",
        {
          type: "text",
          className: "cities-dropdown-search-input",
          placeholder: R,
          value: ye,
          onChange: (p) => {
            ne(p.target.value), p.target.value && ee([]);
          },
          onClick: (p) => p.stopPropagation(),
          onKeyDown: (p) => p.stopPropagation()
        }
      ) }),
      G && ye ? (
        // Show search results
        le.length === 0 ? /* @__PURE__ */ T.jsx("div", { className: "cities-dropdown-empty", children: "No results found" }) : /* @__PURE__ */ T.jsx("ul", { className: "cities-dropdown-list", children: le.map(({ node: p, path: Y }) => /* @__PURE__ */ T.jsxs(
          "li",
          {
            className: `cities-dropdown-item ${p.zones && p.zones.length > 0 ? "has-children" : "leaf"}`,
            role: "option",
            onClick: () => {
              ee(Y), ne(""), (!p.zones || p.zones.length === 0) && ($ && alert(`Selected: ${p.name} (ID: ${p.id})`), l && l(p), v(!1));
            },
            children: [
              /* @__PURE__ */ T.jsx("span", { className: "cities-dropdown-item-name", children: Y.map((te) => te.name).join(" > ") }),
              p.zones && p.zones.length > 0 && /* @__PURE__ */ T.jsx("span", { className: "cities-dropdown-item-arrow", children: "▶" })
            ]
          },
          p.id
        )) })
      ) : (
        // Show normal hierarchical navigation
        /* @__PURE__ */ T.jsxs(T.Fragment, { children: [
          E.length > 0 && /* @__PURE__ */ T.jsx("div", { className: "cities-dropdown-breadcrumb", children: E.map((p, Y) => /* @__PURE__ */ T.jsxs(
            "button",
            {
              className: "cities-dropdown-breadcrumb-item",
              onClick: () => {
                const te = E.slice(0, Y + 1);
                ee(te);
              },
              children: [
                p.name,
                Y < E.length - 1 && " > "
              ]
            },
            p.id
          )) }),
          Ee.length === 0 ? /* @__PURE__ */ T.jsx("div", { className: "cities-dropdown-empty", children: "No options available" }) : /* @__PURE__ */ T.jsx("ul", { className: "cities-dropdown-list", children: Ee.map((p) => /* @__PURE__ */ T.jsxs(
            "li",
            {
              className: `cities-dropdown-item ${p.zones && p.zones.length > 0 ? "has-children" : "leaf"}`,
              role: "option",
              onClick: () => {
                J(p, E.length);
              },
              children: [
                /* @__PURE__ */ T.jsx("span", { className: "cities-dropdown-item-name", children: p.name }),
                p.zones && p.zones.length > 0 && /* @__PURE__ */ T.jsx("span", { className: "cities-dropdown-item-arrow", children: "▶" })
              ]
            },
            p.id
          )) })
        ] })
      )
    ] })
  ] });
}, Ht = ({
  data: k,
  onSelect: l,
  className: M = "",
  dataUrl: q,
  country: ie = "IT",
  language: X = "it",
  placeholder: B = "Select location...",
  username: U,
  password: F,
  popup: G = !1
}) => {
  const { nodes: R, loading: $, error: x } = gt({
    data: k,
    dataUrl: q,
    country: ie,
    language: X,
    username: U,
    password: F
  }), [I, ue] = ge.useState([]);
  ge.useEffect(() => {
    R && ue([]);
  }, [R]);
  const ye = (g) => {
    if (!R) return [];
    if (g === 0)
      return R.zones;
    let v = I[g - 1];
    return v ? v.zones || [] : [];
  }, ne = (g, v) => {
    if (!v) {
      ue((se) => se.slice(0, g));
      return;
    }
    const S = [...I];
    if (S[g] = v, g < S.length && S.splice(g + 1), v.zones && v.zones.length > 0) {
      const se = v.zones[0];
      S[g + 1] = se;
      let ce = se, fe = g + 2;
      for (; ce.zones && ce.zones.length > 0; )
        S[fe] = ce.zones[0], ce = ce.zones[0], fe++;
    }
    ue(S);
    const J = S[S.length - 1];
    J && (!J.zones || J.zones.length === 0) && (G && alert(`Selected: ${J.name} (ID: ${J.id})`), l && l(J));
  }, le = () => {
    let g = 1;
    for (let v = 0; v < I.length; v++) {
      const S = I[v];
      if (S && S.zones && S.zones.length > 0)
        g = v + 2;
      else
        break;
    }
    return g;
  }, E = (g) => Array.isArray(B) && B[g] ? B[g] : `Select level ${g}...`;
  if ($ && !R)
    return /* @__PURE__ */ T.jsx("div", { className: `cities-dropdown loading ${M}`, children: "Loading..." });
  if (x && !R)
    return /* @__PURE__ */ T.jsxs("div", { className: `cities-dropdown error ${M}`, children: [
      "Error: ",
      x
    ] });
  if (!R)
    return /* @__PURE__ */ T.jsx("div", { className: `cities-dropdown loading ${M}`, children: "Loading..." });
  const ee = le();
  return /* @__PURE__ */ T.jsx("div", { className: `cities-dropdown cities-dropdown-cascading ${M}`, children: Array.from({ length: ee }, (g, v) => {
    const S = ye(v), J = I[v], se = v > 0 && !I[v - 1], ce = E(v);
    return /* @__PURE__ */ T.jsxs("div", { className: "cities-dropdown-cascading-level", children: [
      /* @__PURE__ */ T.jsxs("label", { className: "cities-dropdown-cascading-label", children: [
        "Level ",
        v,
        ":"
      ] }),
      /* @__PURE__ */ T.jsxs(
        "select",
        {
          className: "cities-dropdown-cascading-select",
          value: (J == null ? void 0 : J.id) || "",
          onChange: (fe) => {
            const Ee = fe.target.value;
            if (!Ee) {
              ne(v, null);
              return;
            }
            const p = S.find((Y) => Y.id === Ee);
            p && ne(v, p);
          },
          disabled: se || S.length === 0,
          children: [
            /* @__PURE__ */ T.jsx("option", { value: "", children: se ? "Select previous level first" : S.length === 0 ? "No options" : ce }),
            S.map((fe) => /* @__PURE__ */ T.jsx("option", { value: fe.id, children: fe.name }, fe.id))
          ]
        },
        `select-${v}-${ce}`
      )
    ] }, `${v}-${ce}`);
  }) });
}, qt = ({
  model: k = 0,
  ...l
}) => {
  switch (k) {
    case 1:
      return /* @__PURE__ */ T.jsx(Ht, { ...l });
    case 0:
    default:
      return /* @__PURE__ */ T.jsx(Kt, { ...l });
  }
};
export {
  qt as CitiesDropdown
};
